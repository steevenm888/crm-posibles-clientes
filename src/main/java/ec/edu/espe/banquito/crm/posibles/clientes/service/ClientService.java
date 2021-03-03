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
    
    public List<Client> getAllClientes() throws NotFoundException {
        List<Client> clients = this.clientRepo.findAll();
        if(!clients.isEmpty()) {
            return clients;
        } else {
            throw new NotFoundException("There are no clients in the data base yet.");
        }
    }
    
    public void createClient(Client client) throws InsertException {
        Client clientSaved = this.clientRepo.save(client);
        if(clientSaved == null) {
            throw new InsertException("client", "No se pudo ingresar el cliente");
        }
    }
    
    public void createSeveralClients(List<Client> clients) throws InsertException {
        if(clients.size() <= 100) {
            this.clientRepo.saveAll(clients);
        } else {
            
            throw new InsertException("Client", "Hubo un error a; ingresar la lista de clientes", new Throwable("No se puede ingresar mas de 100 registros a las vez, registros que se trato de ingresar: "+clients.size()));
        }
    }
    
    public Client getClientById(String id) throws DocumentNotFoundException {
        Optional<Client> client = this.clientRepo.findById(id);
        if(client.isPresent()) {
            return client.get();
        } else {
            throw new DocumentNotFoundException("Couldn't find a client with the id: "+id);
        }
    }
    
    public Client getClientByIdentification(String identification) throws DocumentNotFoundException {
        Optional<Client> client = this.clientRepo.findByIdentification(identification);
        if(client.isPresent()) {
            return client.get();
        } else {
            throw new DocumentNotFoundException("No se pudo encontrar un cliente con esa cedula "+identification);
        }
    }
    
    public List<Client> getClientByEmail(String email) throws NotFoundException {
        List<Client> clients = this.clientRepo.findByEmail(email);
        if(!clients.isEmpty()) {
            return clients;
        } else {
            log.info("Couldn't find clients with this email: {}", email);
            throw new NotFoundException("Couldn't find any clients with this email: "+email);
        }
    }
    
    public List<Client> getClientsByNames(String names) throws NotFoundException {
        List<Client> clients = this.clientRepo.findByNamesLike(names);
        if(!clients.isEmpty()) {
            return clients;
        } else {
            log.info("Couldn't find clients named as {}", names);
            throw new NotFoundException("Couldn't find clients names as "+names);
        }
    }
    
    public List<Client> getClientsBySurnames(String surnames) throws NotFoundException {
        List<Client> clients = this.clientRepo.findBySurnamesLike(surnames);
        if(!clients.isEmpty()) {
            return clients;
        } else {
            log.info("Couldn't find any clients with {} in its surname", surnames);
            throw new NotFoundException("Couldn't find any clients with "+surnames+" in its surname");
        }
    }
    
    public List<Client> getClientByNamesAndSurnames(String names, String surnames) throws NotFoundException {
        List<Client> clients = this.clientRepo.findByNamesAndSurnamesLike(names, surnames);
        if(!clients.isEmpty()) {
            return clients;
        } else {
            log.info("Couldn't find a client that matches {} {} in it's names and surnames");
            throw new NotFoundException("Couldn't find a client that matches "+names+" "+surnames+" in it's names and surnames");
        }
    }
}
