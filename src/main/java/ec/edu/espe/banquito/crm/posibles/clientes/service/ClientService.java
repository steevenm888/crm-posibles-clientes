/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.service;

import ec.edu.espe.banquito.crm.posibles.clientes.repository.ClientRepository;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentNotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.InsertException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.NotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author esteban
 */
@Service
@Slf4j
public class ClientService {

    private final ClientRepository clientRepo;

    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public List<Client> listarClientes() {
        return this.clientRepo.findAll();
    }

    public void crearCliente(Client client) throws InsertException, DocumentNotFoundException {
        Optional<Client> searchedClient = this.clientRepo.findByIdentification(client.getIdentification());
        if (searchedClient.isPresent()) {
            log.error("Can't insert a new client when the identification {} already exists in another registry");
            throw new DocumentNotFoundException("The client with id " + client.getIdentification() + " already exists");
        } else {
            Client clientSaved = this.clientRepo.save(client);
            if (clientSaved == null) {
                throw new InsertException("client", "No se pudo ingresar el cliente");
            }
        }
    }

    public void crearVariosClientes(List<Client> clients) throws InsertException, DocumentAlreadyExistsException {
        Integer originalClientsSize = clients.size();
        List<String> identifications = new ArrayList<>();
        List<Client> clientsToRemove = new ArrayList<>();
        if (clients.size() > 0 && clients.size() <= 100) {
            clients.forEach(client -> {
                Optional<Client> searchedClient = this.clientRepo.findByIdentification(client.getIdentification());
                if (searchedClient.isPresent()) {
                    clientsToRemove.add(client);
                    identifications.add(client.getIdentification());
                }
            });
            clients.removeAll(clientsToRemove);
            this.clientRepo.saveAll(clients);
            if (clients.size() < originalClientsSize) {
                log.error("Couldn't insert all the registries, only {} "
                        + "There was problems with the following identifications which already exist in other registries: {}",
                        clients.size(), identifications.toString());
                throw new DocumentAlreadyExistsException("Couldn't insert all the registries, only " + clients.size() + ""
                        + "There was problems with the following identifications which already exist in other registries:"
                        + identifications.toString());
            }
        } else {
            log.error("Can't save more than 100 documents at a time");
            throw new InsertException("Client", "Can't save the documents", new Throwable("Can't save more than 100 documents at a time, number of sent registries: " + clients.size()));
        }
    }

    public Client obtenerClientePorId(String id) throws DocumentNotFoundException {
        Optional<Client> client = this.clientRepo.findById(id);
        if (client.isPresent()) {
            return client.get();
        } else {
            throw new DocumentNotFoundException("No se encontro el cliente con id: " + id);
        }
    }

    public Client obtenerClientePorCedula(String identification) throws DocumentNotFoundException {
        Optional<Client> client = this.clientRepo.findByIdentification(identification);
        if (client.isPresent()) {
            return client.get();
        } else {
            throw new DocumentNotFoundException("No se pudo encontrar un cliente con esa cedula " + identification);
        }
    }

    public List<Client> getClientsByNames(String names) throws NotFoundException {
        List<Client> clients = this.clientRepo.findByNamesLike(names);
        if (!clients.isEmpty()) {
            return clients;
        } else {
            log.info("Couldn't find clients named as {}", names);
            throw new NotFoundException("Couldn't find clients names as " + names);
        }
    }

    public List<Client> getClientsBySurnames(String surnames) throws NotFoundException {
        List<Client> clients = this.clientRepo.findBySurnamesLike(surnames);
        if (!clients.isEmpty()) {
            return clients;
        } else {
            log.info("Couldn't find any clients with {} in its surname", surnames);
            throw new NotFoundException("Couldn't find any clients with " + surnames + " in its surname");
        }
    }

    public List<Client> getClientByNamesAndSurnames(String names, String surnames) throws NotFoundException {
        List<Client> clients = this.clientRepo.findByNamesIgnoringCaseLikeAndSurnamesIgnoringCaseLike(names, surnames);
        if (!clients.isEmpty()) {
            return clients;
        } else {
            log.info("Couldn't find a client that matches {} {} in it's names and surnames");
            throw new NotFoundException("Couldn't find a client that matches " + names + " " + surnames + " in it's names and surnames");
        }
    }

}
