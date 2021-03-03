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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author esteban
 */

@CrossOrigin
@RestController
@RequestMapping("/api/posibles-clientes/cliente")
@Slf4j
public class ClientController {
    private final ClientService service;
    
    public ClientController(ClientService service) {
        this.service = service;
    }
    
    @GetMapping
    @ApiOperation(value = "Listar todos los posibles clientes")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Todos los posibles clientes listados"),
                            @ApiResponse(code = 204, message = "No existe ningún registro a mostrarse"),
                            @ApiResponse(code = 500, message = "Error interno del servidor")})
    public ResponseEntity<List<Client>> listarTodos() {
        try {
            return ResponseEntity.ok(this.service.getAllClientes());
        } catch (NotFoundException e) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping(path = "/id/{id}")
    @ApiOperation(value = "Listar posible cliente por su id")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Posible cliente con el id enviado fue encontrado"),
                            @ApiResponse(code = 404, message = "No se encontró ningún posible cliente con el id enviado"),
                            @ApiResponse(code = 500, message = "Error interno del servidor")})
    public ResponseEntity<Client> getById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(this.service.getClientById(id));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception  e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping(path = "/cedula/{identification}")
    @ApiOperation(value = "Listar posible cliente por su cedula")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Posible cliente con la cedula enviado fue encontrado"),
                            @ApiResponse(code = 404, message = "No se encontró ningún posible cliente con el id enviado"),
                            @ApiResponse(code = 500, message = "Error interno del servidor")})
    public ResponseEntity<Client> getByCedula(@PathVariable("identification") String identification) {
        try {
            return ResponseEntity.ok(this.service.getClientByIdentification(identification));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    @ApiOperation(value = "Crear posible cliente")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Posible cliente creado"),
                            @ApiResponse(code = 400, message = "Datos enviados erroneamente, error al insertar"),
                            @ApiResponse(code = 500, message = "Error interno del servidor")})
    public ResponseEntity crear(@RequestBody ClientRQ client) {
        try {
            this.service.createClient(Client.builder()
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/varios")
    @ApiOperation(value = "Crear posible cliente")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Lista de posibles clientes guardada"),
                            @ApiResponse(code = 400, message = "Lista de posibles clientes con problemas"),
                            @ApiResponse(code = 500, message = "Error interno del servidor")})
    public ResponseEntity createSeveral(@RequestBody List<ClientRQ> clients) {
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
            this.service.createSeveralClients(clientsList);
            return ResponseEntity.ok().build();
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/byEmail")
    @ApiOperation(value = "Obtener lista de posibles clientes por email")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Lista de posibles clientes por email encontrada"),
                            @ApiResponse(code = 404, message = "Clientes con el email especificado no encontrados"),
                            @ApiResponse(code = 500, message = "Error interno del servidor")})
    public ResponseEntity<List<Client>> getClientsByEmail(@RequestParam String email) {
        try {
            log.info("Retrived all clients with email: {}", email);
            return ResponseEntity.ok(this.service.getClientByEmail(email));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/byNamesSurnames")
    @ApiOperation(value = "Obtener lista de posibles clientes por nombres y apellidos")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Lista de posibles clientes por nombres y apellidos no encontrada"),
                            @ApiResponse(code = 404, message = "Clientes con el email especificado no encontrados"),
                            @ApiResponse(code = 500, message = "Error interno del servidor")})
    public ResponseEntity<List<Client>> getClientsByNamesSrunames(@RequestBody ClientNamesSurnamesRQ clientNamesSurnames) {
        try {
            if(clientNamesSurnames.getNames() != null && clientNamesSurnames.getSurnames() == null){
                log.info("Retrieved all clients named as {}", clientNamesSurnames.getNames());
                return ResponseEntity.ok(this.service.getClientsByNames(clientNamesSurnames.getNames()));
            } else if(clientNamesSurnames.getNames() == null && clientNamesSurnames.getSurnames() != null) {
                log.info("Retrieved all clients with {} in it's surnames", clientNamesSurnames.getSurnames());
                return ResponseEntity.ok(this.service.getClientsBySurnames(clientNamesSurnames.getSurnames()));
            } else if(clientNamesSurnames.getNames() != null && clientNamesSurnames.getSurnames() != null) {
                log.info("Retrieved all clients with {} {} in it's names and surnames", clientNamesSurnames.getSurnames());
                return ResponseEntity.ok(this.service.getClientByNamesAndSurnames(clientNamesSurnames.getNames(), clientNamesSurnames.getSurnames()));
            } else {
                log.error("Not enough data to perform the search");
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
