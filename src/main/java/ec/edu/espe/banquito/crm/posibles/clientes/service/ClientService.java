package ec.edu.espe.banquito.crm.posibles.clientes.service;

import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.BuroRs;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentAlreadyExistsException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentNotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.InsertException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.NotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import ec.edu.espe.banquito.crm.posibles.clientes.repository.ClientRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientService {

    private final ClientRepository clientRepo;

    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public List<Client> getAllClientes() throws NotFoundException {
        List<Client> clients = this.clientRepo.findAll();
        if (!clients.isEmpty()) {
            return clients;
        } else {
            throw new NotFoundException("There are no clients in the data base yet.");
        }
    }

    public void createClient(Client client) throws InsertException, DocumentNotFoundException {
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

    public List<Client> createSeveralClients(
            List<Client> clients) throws InsertException,
            DocumentAlreadyExistsException {
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
                log.warn("Couldn't insert all the registries, only {} "
                        + "There was problems with the following identifications "
                        + "which already exist in other registries: {}",
                        clients.size(), identifications.toString());
            }
            return clients;
        } else {
            log.error("Can't save more than 100 documents at a time");
            throw new InsertException(
                    "Client",
                    "Can't save the documents",
                    new Throwable("Can't save more than 100 documents at a time, number of sent registries: " 
                            + clients.size()));
        }
    }

    public Client getClientById(String id) throws DocumentNotFoundException {
        Optional<Client> client = this.clientRepo.findById(id);
        if (client.isPresent()) {
            return client.get();
        } else {
            log.info("Couldn't find a client with the id {}", id);
            throw new DocumentNotFoundException("Couldn't find a client with the id: " + id);
        }
    }

    public Client getClientByIdentification(String identification) throws DocumentNotFoundException {
        Optional<Client> client = this.clientRepo.findByIdentification(identification);
        if (client.isPresent()) {
            return client.get();
        } else {
            log.info("Couldn't find a client with {} as identification.", identification);
            throw new DocumentNotFoundException("Couldn't find possible client with identification " + identification);
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
            throw new NotFoundException(
                    "Couldn't find a client that matches " 
                            + names 
                            + " " 
                            + surnames 
                            + " in it's names and surnames");
        }
    }

    public List<Client> transformBuroRsToClient(List<BuroRs> responseBody) {
        List<Client> clientsList = new ArrayList<>();
        for (BuroRs client : responseBody) {
            clientsList.add(Client.builder()
                    .identification(client.getPersona().getCedula())
                    .names(client.getPersona().getNombres())
                    .surnames(client.getPersona().getApellidos())
                    .genre(client.getPersona().getGenero())
                    .birthdate(client.getPersona().getFechaNacimiento())
                    .nationality(client.getPersona().getNacionalidad().getNombre())
                    .rating(client.getCalificacion())
                    .amountOwed(client.getCantidadAdeudada())
                    .alternateRating(client.getCalificacionAlterna())
                    .build());
        }
        return clientsList;
    }
}
