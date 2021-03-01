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
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.BuroRS;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.ClientRQ;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentAlreadyExistsException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentNotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.InsertException;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import java.util.ArrayList;
import java.util.List;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author esteban
 */
@CrossOrigin
@RestController
@RequestMapping("/api/possible-clients")
@Slf4j
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation(value = "List clients")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity listarTodos() {
        return ResponseEntity.ok(this.service.listarClientes());
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Find client by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<Client> listarPorId(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(this.service.obtenerClientePorId(id));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/identification/{identification}")
    @ApiOperation(value = "Find client by identification")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<Client> listarPorCedula(@PathVariable("identification") String identification) {
        try {
            return ResponseEntity.ok(this.service.obtenerClientePorCedula(identification));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ApiOperation(value = "Create possible client")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Client created successfully"),
        @ApiResponse(code = 400, message = "The data passed is not correct"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity crear(@RequestBody ClientRQ client) {
        try {
            this.service.crearCliente(Client.builder()
                    .identification(client.getIdentification())
                    .names(client.getNames())
                    .surnames(client.getSurnames())
                    .genre(client.getGenre())
                    .birthdate(client.getBirthdate())
                    .nationality(client.getNationality()).build());
            return ResponseEntity.ok().build();
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/byNamesSurnames/{names}-{surnames}")
    @ApiOperation(value = "Find client name and surname")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 400, message = "Not enough data to perform the search"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<Client>> getClientsByNamesSrunames(@PathVariable String names, @PathVariable String surnames) {
        try {
            if (names != null && surnames == null) {
                log.info("Retrieved all clients named as {}", names);
                return ResponseEntity.ok(this.service.getClientsByNames(names));
            } else if (names == null && surnames != null) {
                log.info("Retrieved all clients with {} in it's surnames", surnames);
                return ResponseEntity.ok(this.service.getClientsBySurnames(surnames));
            } else if (names != null && surnames != null) {
                log.info("Retrieved all clients with {} {} in it's names and surnames", names, surnames);
                return ResponseEntity.ok(this.service.getClientByNamesAndSurnames(names, surnames));
            } else {
                log.error("Not enough data to perform the search");
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createClientsFromBuroRating")
    @ApiOperation(value = "Create various clients from Buro with given rating")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Created successfully"),
        @ApiResponse(code = 400, message = "The data passed is not correct in format"),
        @ApiResponse(code = 404, message = "No data found in Buro")})
    public ResponseEntity createClientsFromBuro(@RequestBody String rating) {
        List<BuroRS> responseBody = Unirest.get("http://3.227.175.235:8082/api/bbConsultas/buro/calificacion/{rating}")
                .routeParam("rating", rating)
                .asObject(new GenericType<List<BuroRS>>() {
                })
                .getBody();
        ResponseEntity response;
        if (responseBody != null && responseBody.size() > 0) {
            List<Client> clientsList = new ArrayList<>();
            for (BuroRS client : responseBody) {
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
            try {
                this.service.crearVariosClientes(clientsList);
                response = ResponseEntity.ok().build();
            } catch (InsertException e) {
                response = ResponseEntity.badRequest().build();
            } catch (DocumentAlreadyExistsException e) {
                response = ResponseEntity.badRequest().build();
            }
        } else {
            response = ResponseEntity.notFound().build();;
        }
        return response;
    }
}
