package ec.edu.espe.banquito.crm.posibles.clientes.api;

import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.BuroRs;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.ClientRq;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.RatingOwedRq;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentAlreadyExistsException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentNotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.InsertException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.NotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import ec.edu.espe.banquito.crm.posibles.clientes.service.ClientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;
import java.util.List;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(value = "List all possible clients")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All possible clients listed"),
        @ApiResponse(code = 204, message = "There are no registries to show"),
        @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<List<Client>> listarTodos() {
        try {
            return ResponseEntity.ok(this.service.getAllClientes());
        } catch (NotFoundException e) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Listar posible cliente por su id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Possible client with sent id found"),
        @ApiResponse(code = 404, message = "No possible client with sent id found"),
        @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<Client> getById(@PathVariable("id") String id) {

        try {
            return ResponseEntity.ok(this.service.getClientById(id));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/identification/{identification}")
    @ApiOperation(value = "Listar posible cliente por su cedula")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Possible client with sent identification found"),
        @ApiResponse(code = 404, message = "No possible client found with sent id"),
        @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<Client> getByIdentification(@PathVariable("identification") String identification) {
        try {
            return ResponseEntity.ok(this.service.getClientByIdentification(identification));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @ApiOperation(value = "Create possible client")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Client created successfully"),
        @ApiResponse(code = 400, message = "The privided data is not correct"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internalserver error")})
    public ResponseEntity crear(@RequestBody ClientRq client) {
        try {
            this.service.createClient(Client.builder()
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNamesSurnames/{names}-{surnames}")
    @ApiOperation(value = "Find client name and surname")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 400, message = "Not enough data to perform the search"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<Client>> getClientsByNamesSrunames(
            @PathVariable String names,
            @PathVariable String surnames) {
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createClientsFromBuroRating")
    @ApiOperation(value = "Create various clients from Buro with given rating")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Created successfully"),
        @ApiResponse(code = 400, message = "The data passed is not correct in format"),
        @ApiResponse(code = 404, message = "No data found in Buro")})
    public ResponseEntity<List<Client>> createClientsFromBuroRating(@RequestBody String rating) {
        List<BuroRs> responseBody = Unirest.get("http://bbconsultas.southcentralus.cloudapp.azure.com:8082/api/bbConsultas/buro/calificacion/{rating}")
                .routeParam("rating", rating)
                .asObject(new GenericType<List<BuroRs>>() {
                })
                .getBody();
        ResponseEntity response;
        if (responseBody != null && responseBody.size() > 0) {
            List<Client> clientsList = this.service.transformBuroRsToClient(responseBody);
            try {
                response = ResponseEntity.ok(this.service.createSeveralClients(clientsList));
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

    @PostMapping("/createClientsFromBuroOwed")
    @ApiOperation(value = "Create various clients from Buro that owe less than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Created successfully"),
        @ApiResponse(code = 400, message = "The data passed is not correct in format"),
        @ApiResponse(code = 404, message = "No data found in Buro")})
    public ResponseEntity<List<Client>> createClientsFromBuroOwed(@RequestBody BigDecimal amountOwed) {
        List<BuroRs> responseBody = Unirest.get("http://bbconsultas.southcentralus.cloudapp.azure.com:8082/api/bbConsultas/buro/cantidadAdeudada/{amountOwed}")
                .routeParam("amountOwed", amountOwed.toString())
                .asObject(new GenericType<List<BuroRs>>() {
                })
                .getBody();
        ResponseEntity response;
        if (responseBody != null && responseBody.size() > 0) {
            List<Client> clientsList = this.service.transformBuroRsToClient(responseBody);
            try {
                response = ResponseEntity.ok(this.service.createSeveralClients(clientsList));
            } catch (InsertException e) {
                response = ResponseEntity.badRequest().build();
            } catch (DocumentAlreadyExistsException e) {
                response = ResponseEntity.notFound().build();
            }
        } else {
            response = ResponseEntity.notFound().build();;
        }
        return response;
    }

    @PostMapping("/createClientsFromBuroOwedAndRating")
    @ApiOperation(value = "Create various clients from Buro that owe less than given value and have the given rating")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Created successfully"),
        @ApiResponse(code = 400, message = "The data passed is not correct in format"),
        @ApiResponse(code = 404, message = "No data found in Buro")})
    public ResponseEntity<List<Client>> createClientsFromBuroOwedAndRating(@RequestBody RatingOwedRq ratingOwedRq) {
        List<BuroRs> responseBody = Unirest.get("http://bbconsultas.southcentralus.cloudapp.azure.com:8082/api/bbConsultas/buro/calificacionAndAdeudada")
                .queryString("calificacion", ratingOwedRq.getRating())
                .queryString("cantidadAdeudada", ratingOwedRq.getAmountOwed())
                .asObject(new GenericType<List<BuroRs>>() {
                })
                .getBody();
        log.info("Data retrieved from buro {}", responseBody);
        ResponseEntity response;
        if (responseBody != null && responseBody.size() > 0) {
            List<Client> clientsList = this.service.transformBuroRsToClient(responseBody);
            try {
                response = ResponseEntity.ok(this.service.createSeveralClients(clientsList));
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
