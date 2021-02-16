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
import java.util.List;
import java.util.Optional;

/**
 *
 * @author esteban
 */
public class ClientService {
    private final ClientRepository clientRepo;
    
    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }
    
    public List<Client> listarClientes() {
        return this.clientRepo.findAll();
    }
    
    public void crearCliente(Client client) throws InsertException {
        Client clientSaved = this.clientRepo.save(client);
        if(clientSaved == null) {
            throw new InsertException("client", "No se pudo ingresar el cliente");
        }
    }
    
    public void crearVariosClientes(List<Client> clients) throws InsertException {
        if(clients.size() <= 100) {
            this.clientRepo.saveAll(clients);
        } else {
            
            throw new InsertException("Client", "Hubo un error a; ingresar la lista de clientes", new Throwable("No se puede ingresar mas de 100 registros a las vez, registros que se trato de ingresar: "+clients.size()));
        }
    }
    
    public Client obtenerClientePorId(String id) throws DocumentNotFoundException {
        Optional<Client> client = this.clientRepo.findById(id);
        if(client.isPresent()) {
            return client.get();
        } else {
            throw new DocumentNotFoundException("No se encontro el cliente con id: "+id);
        }
    }
    
    public Client obtenerClientePorCedula(String identification) throws DocumentNotFoundException {
        Optional<Client> client = this.clientRepo.findByIdentification(identification);
        if(client.isPresent()) {
            return client.get();
        } else {
            throw new DocumentNotFoundException("No se pudo encontrar un cliente con esa cedula "+identification);
        }
    }
}
