package ec.edu.espe.banquito.crm.posibles.clientes.api;

import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.BuroRs;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.ClientRq;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.PaisRs;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.PersonaRs;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentAlreadyExistsException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentNotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.InsertException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.NotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import ec.edu.espe.banquito.crm.posibles.clientes.repository.ClientRepository;
import ec.edu.espe.banquito.crm.posibles.clientes.service.ClientService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTests {
    
    @Mock
    private ClientService service;
    private List<Client> clientList;
    private Client clientSample1;
    private Client clientSample2;
    
    @InjectMocks
    private ClientController controller;
    
    @BeforeEach
    public void setUp() {
        clientSample1 = new Client();
        clientSample2 = new Client();
        clientList = new ArrayList<>();
        clientList.add(clientSample1);
        clientList.add(clientSample2);
    }
    
    @Test
    public void GivenGetClientsReturnResponseEntityOkWithListOfClients() {
        when(service.getAllClientes()).thenReturn(clientList);
        Assertions.assertEquals(ResponseEntity.ok(clientList), controller.listarTodos());
    }
    
    @Test
    public void GivenGetClientsAndNoRegistriesFoundReturnResponseEntityNoContent() {
        when(service.getAllClientes()).thenThrow(new NotFoundException(""));
        Assertions.assertEquals(ResponseEntity.noContent().build(), controller.listarTodos());
    }
    
    @Test
    public void GivenGetClientsAndErrorOcurredReturnResponseEntityStatusInternalServerError() {
        when(service.getAllClientes()).thenThrow(new RuntimeException());
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.listarTodos());
    }
    
    @Test
    public void GivenIdReturnResponseEntityOkWithClient() {
        try {
            when(service.getClientById("1261743gdvfwhegdfv")).thenReturn(clientSample1);
            Assertions.assertEquals(ResponseEntity.ok(clientSample1), controller.getById("1261743gdvfwhegdfv"));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void GivenIdThatNotExistsReturnResponseEntityNoContent() {
        try {
            when(service.getClientById("1261743gdvfwhegd21243")).thenThrow(new DocumentNotFoundException(""));
            Assertions.assertEquals(ResponseEntity.notFound().build(), controller.getById("1261743gdvfwhegd21243"));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void GivenIdReturnResponseEntityStatusInternalServerError() {
        try {
            when(service.getClientById("1261743gdvfwhegd245637245")).thenThrow(new RuntimeException());
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.getById("1261743gdvfwhegd245637245"));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenIdentificationReturnResponseEntityOkWithClient() {
        try {
            when(service.getClientByIdentification("1724217367")).thenReturn(clientSample1);
            Assertions.assertEquals(ResponseEntity.ok(clientSample1), controller.getByIdentification("1724217367"));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void GivenIdentificationThatNotExistsReturnResponseEntityNoContent() {
        try {
            when(service.getClientByIdentification("1724217366")).thenThrow(new DocumentNotFoundException(""));
            Assertions.assertEquals(ResponseEntity.notFound().build(), controller.getByIdentification("1724217366"));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test
    public void GivenIdentificationReturnResponseEntityStatusInternalServerError() {
        try {
            when(service.getClientByIdentification("1724217365")).thenThrow(new RuntimeException());
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.getByIdentification("1724217365"));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenClientRqReturnResponseEntityOk() {
        ClientRq clientRqSample = new ClientRq();
        Assertions.assertEquals(ResponseEntity.ok().build(), controller.crear(clientRqSample));
    }
    
    @Test
    public void GivenClientRqMalFormedReturnResponseEntityBadRequest() {
        ClientRq clientRqSample = new ClientRq();
        try {
            doThrow(InsertException.class).when(service).createClient(any());
            Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.crear(clientRqSample));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenClientRqthatExistsReturnResponseEntityBadRequest() {
        ClientRq clientRqSample = new ClientRq();
        try {
            doThrow(DocumentAlreadyExistsException.class).when(service).createClient(any());
            Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.crear(clientRqSample));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenClientRqwithErrorReturnResponseEntityBadRequest() {
        ClientRq clientRqSample = new ClientRq();
        try {
            doThrow(RuntimeException.class).when(service).createClient(any());
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.crear(clientRqSample));
        } catch (Exception ex) {
            Logger.getLogger(ClientControllerTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenNamesAndSurnamesReturnResponseEntityOkListOfClients() {
        when(service.getClientByNamesAndSurnames("KAREN", "COFRE")).thenReturn(clientList);
        Assertions.assertEquals(ResponseEntity.ok(clientList), controller.getClientsByNamesSrunames("KAREN", "COFRE"));
    }
    
    @Test
    public void GivenNamesReturnResponseEntityOkListOfClients() {
        when(service.getClientsByNames("KAREN")).thenReturn(clientList);
        Assertions.assertEquals(ResponseEntity.ok(clientList), controller.getClientsByNamesSrunames("KAREN", null));
    }
    
    @Test
    public void GivenSurnamesReturnResponseEntityOkListOfClients() {
        when(service.getClientsBySurnames("COFRE")).thenReturn(clientList);
        Assertions.assertEquals(ResponseEntity.ok(clientList), controller.getClientsByNamesSrunames(null, "COFRE"));
    }
    
    @Test
    public void GivenNoParametersReturnResponseEntityBadRequest() {
        Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.getClientsByNamesSrunames(null, null));
    }
    
    @Test
    public void GivenNamesOrSurnamesNotExistsReturnResponseEntityNotFound() {
        when(service.getClientByNamesAndSurnames("ASDFG", "ASDFG")).thenThrow(new NotFoundException(""));
        Assertions.assertEquals(ResponseEntity.notFound().build(), controller.getClientsByNamesSrunames("ASDFG", "ASDFG"));
    }
    
    @Test
    public void GivenNamesOrSurnamesWithErrorReturnResponseEntityInternalServerError() {
        when(service.getClientByNamesAndSurnames("", "")).thenThrow(new RuntimeException());
        Assertions.assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(), controller.getClientsByNamesSrunames("", ""));
    }
    
//    @Test
//    public void GivenBuroRatingReturnResponseEntityOkListOfClients() {
//        PersonaRs personaSample = new PersonaRs();
//        PaisRs nacionalidadSample = new PaisRs();
//        personaSample.setNacionalidad(nacionalidadSample);
//        BuroRs buroRsSample1 = new BuroRs();
//        buroRsSample1.setPersona(personaSample);
//        BuroRs buroRsSample2 = new BuroRs();
//        buroRsSample2.setPersona(personaSample);
//        List<BuroRs> newListBuroRs = new ArrayList<>();
//        newListBuroRs.add(buroRsSample1);
//        newListBuroRs.add(buroRsSample2);
//        when(Unirest.get("http://bbconsultas.southcentralus.cloudapp.azure.com:8082/api/bbConsultas/buro/calificacion/{rating}")
//                .routeParam("rating", "VER")
//                .asObject(new GenericType<List<BuroRs>>() {
//                })
//                .getBody()).thenReturn(newListBuroRs);
//        Assertions.assertEquals(ResponseEntity.ok(clientList), controller.createClientsFromBuroRating("VER"));
//    }
    
}
