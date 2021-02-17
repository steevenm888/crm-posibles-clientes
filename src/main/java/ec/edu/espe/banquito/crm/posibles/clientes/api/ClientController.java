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
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentNotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.InsertException;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import java.util.ArrayList;
import java.util.List;

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
                                      .nationality(client.getNationality())
                                      .contributor(client.getContributor()).build());
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
                                      .genre(client.getGenre())
                                      .birthdate(client.getBirthdate())
                                      .phones(client.getPhones())
                                      .addresses(client.getAddresses())
                                      .email(client.getEmail())
                                      .nationality(client.getNationality())
                                      .contributor(client.getContributor()).build());
            }
            this.service.crearVariosClientes(clientsList);
            return ResponseEntity.ok().build();
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
