/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.api;

import ec.edu.espe.banquito.crm.posibles.clientes.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.ClientRQ;
import ec.edu.espe.banquito.crm.posibles.clientes.enums.GenreEnum;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentNotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.InsertException;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import java.util.ArrayList;
import java.util.List;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.ClientNamesSurnamesRQ;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.NotFoundException;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author esteban
 */

@RestController
@RequestMapping("/api/posibles-clientes/cliente")
@Slf4j
public class ClientController {
    private final ClientService service;
    
    public ClientController(ClientService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity listarTodos() {
        return ResponseEntity.ok(this.service.listarClientes());
    }
    
    @GetMapping(path = "/id/{id}")
    public ResponseEntity listarPorId(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(this.service.obtenerClientePorId(id));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping(path = "/cedula/{identification}")
    public ResponseEntity listarPorCedula(@PathVariable("identification") String identification) {
        try {
            return ResponseEntity.ok(this.service.obtenerClientePorCedula(identification));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity crear(@RequestBody ClientRQ client) {
        try {
            this.service.crearCliente(Client.builder()
                                      .identification(client.getIdentification())
                                      .names(client.getNames())
                                      .surnames(client.getSurnames())
                                      .genre(client.getGenre())
                                      .birthdate(client.getBirthdate())
                                      .phones(client.getPhones())
                                      .addresses(client.getAddresses())
                                      .email(client.getEmail())
                                      .nationality(client.getNationality()).build());
            return ResponseEntity.ok().build();
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/varios")
    public ResponseEntity crearVarios(@RequestBody List<ClientRQ> clients) {
        try {
            List<Client> clientsList = new ArrayList<>();
            for (ClientRQ client : clients) {
                clientsList.add(Client.builder()
                                      .identification(client.getIdentification())
                                      .names(client.getNames())
                                      .surnames(client.getSurnames())
                                      .genre(GenreEnum.valueOf(client.getGenre()).getCode())
                                      .birthdate(client.getBirthdate())
                                      .phones(client.getPhones())
                                      .addresses(client.getAddresses())
                                      .email(client.getEmail())
                                      .nationality(client.getNationality()).build());
            }
            this.service.crearVariosClientes(clientsList);
            return ResponseEntity.ok().build();
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/byEmail")
    public ResponseEntity getClientsByEmail(@RequestParam String email) {
        try {
            log.info("Retrived all clients with email: {}", email);
            return ResponseEntity.ok(this.service.getClientByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/byNamesSurnames")
    public ResponseEntity getClientsByNamesSrunames(@RequestBody ClientNamesSurnamesRQ clientNamesSurnames) {
        try {
            if(clientNamesSurnames.getNames() != null && clientNamesSurnames.getSurnames() == null){
                log.info("Retrieved all clients named as {}", clientNamesSurnames.getNames());
                return ResponseEntity.ok(this.service.getClientsByNames(clientNamesSurnames.getNames()));
            } else if(clientNamesSurnames.getNames() == null && clientNamesSurnames.getSurnames() != null) {
                log.info("Retrieved all clients with {} in it's surnames", clientNamesSurnames.getSurnames());
                return ResponseEntity.ok(this.service.getClientsBySurnames(clientNamesSurnames.getSurnames()));
            } else {
                log.error("Not enough data to perform the search");
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
