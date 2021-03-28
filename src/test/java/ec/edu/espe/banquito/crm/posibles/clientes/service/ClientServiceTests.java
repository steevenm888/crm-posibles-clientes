package ec.edu.espe.banquito.crm.posibles.clientes.service;

import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.BuroRs;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.PaisRs;
import ec.edu.espe.banquito.crm.posibles.clientes.api.dto.PersonaRs;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentAlreadyExistsException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.DocumentNotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.InsertException;
import ec.edu.espe.banquito.crm.posibles.clientes.exception.NotFoundException;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import ec.edu.espe.banquito.crm.posibles.clientes.repository.ClientRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {
    
    @Mock
    private ClientRepository clientRepository;
    
    @InjectMocks
    private ClientService service;
    private Client clientSample1;
    private Client clientSample2;
    private List<Client> clientList;
    
    @BeforeEach
    public void setUp() {
        this.clientSample1 = new Client();
        this.clientSample2 = new Client();
        this.clientList = new ArrayList<>();
        this.clientList.add(clientSample1);
        this.clientList.add(clientSample2);
    }
    
    @Test
    public void GivenListClientsReturnListOfAllClients() {
        when(clientRepository.findAll()).thenReturn(clientList);
        List<Client> clientListTest = service.getAllClientes();
        Assertions.assertEquals(clientList, clientListTest);
    }
    
    @Test
    public void GivenListClientsAndNoClientsFoundThrowNotFoundException() {
        when(clientRepository.findAll()).thenReturn(new ArrayList<Client>());
        Assertions.assertThrows(NotFoundException.class, () -> service.getAllClientes());
    }
    
    @Test
    public void GivenClientCreateClient() {
        clientSample1 = Client.builder()
                .names("KAREN SHAKIRA")
                .alternateRating(BigDecimal.ZERO)
                .amountOwed(BigDecimal.ZERO)
                .birthdate(new Date().toString())
                .genre("FEM")
                .identification("1724217367")
                .nationality("ECUATORIANA")
                .rating("VER")
                .surnames("COFRE MALDONADO")
                .build();
        try {
            service.createClient(clientSample1);
        } catch (InsertException ex) {
            Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenClientAndClientWasFoundThrowDocumentAlreadyExistsException() {
        clientSample1 = Client.builder()
                .names("KAREN SHAKIRA")
                .alternateRating(BigDecimal.ZERO)
                .amountOwed(BigDecimal.ZERO)
                .birthdate(new Date().toString())
                .genre("FEM")
                .identification("1724217367")
                .nationality("ECUATORIANA")
                .rating("VER")
                .surnames("COFRE MALDONADO")
                .build();
        when(clientRepository.findByIdentification("1724217367")).thenReturn(Optional.of(clientSample1));
        Assertions.assertThrows(DocumentAlreadyExistsException.class, () -> service.createClient(clientSample1));
    }
    
    @Test
    public void GivenClientAndProducedErrorCreatingClientThrowInsertException() {
        when(clientRepository.save(any())).thenReturn(null);
        Assertions.assertThrows(InsertException.class, () -> service.createClient(clientSample1));
    }
    
    @Test
    public void GivenListOfClientsReturnListOfClients() {
        clientSample1 = Client.builder()
                .names("ALAN QUIMBITA")
                .alternateRating(BigDecimal.ZERO)
                .amountOwed(BigDecimal.ZERO)
                .birthdate(new Date().toString())
                .genre("MAS")
                .identification("1723549081")
                .nationality("ECUATORIANA")
                .rating("VER")
                .surnames("COFRE MALDONADO")
                .build();
        clientList.add(clientSample1);
        clientSample2 = Client.builder()
                .names("ESTEBAN MOLINA")
                .alternateRating(BigDecimal.ZERO)
                .amountOwed(BigDecimal.ZERO)
                .birthdate(new Date().toString())
                .genre("MAS")
                .identification("1723415267")
                .nationality("ECUATORIANA")
                .rating("VER")
                .surnames("MOLINA LESCANO")
                .build();
        clientList.add(clientSample2);
        try {
            List<Client> newListCLientTest = this.service.createSeveralClients(clientList);
            Assertions.assertEquals(clientList, newListCLientTest);
        } catch (Exception ex) {
            Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test 
    public void GivenListOf101ClientsThrowInsertException() {
        for(int i = 0; i < 101; i++) {
            clientList.add(clientSample1);
        }
        Assertions.assertThrows(InsertException.class, () -> service.createSeveralClients(clientList));
    }
    
    @Test
    public void GivenIdReturnClient() {
        when(clientRepository.findById("12er3416253eeertyy324")).thenReturn(Optional.of(clientSample1));
        try {
            Assertions.assertEquals(clientSample1, service.getClientById("12er3416253eeertyy324"));
        } catch (Exception ex) {
            Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenIdThatNotExistsThrowDocumentNotFoundException() {
        when(clientRepository.findById("12er3416253eeertyy541")).thenReturn(Optional.empty());
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getClientById("12er3416253eeertyy541"));
    }
    
    @Test
    public void GivenIdentificationReturnClient() {
        when(clientRepository.findByIdentification("1724217367")).thenReturn(Optional.of(clientSample1));
        try {
            Assertions.assertEquals(clientSample1, service.getClientByIdentification("1724217367"));
        } catch (Exception ex) {
            Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenIdentificationThatNotExistsThrowDocumentNotFoundException() {
        when(clientRepository.findByIdentification("1724217366")).thenReturn(Optional.empty());
        Assertions.assertThrows(DocumentNotFoundException.class, () -> service.getClientByIdentification("1724217366"));
    }
    
    @Test
    public void GivenClientNameReturnListOfClients() {
        when(clientRepository.findByNamesLike("KAREN")).thenReturn(clientList);
        try {
            List<Client> newListCLientTest = this.service.getClientsByNames("KAREN");
            Assertions.assertEquals(clientList, newListCLientTest);
        } catch (Exception ex) {
            Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenNamesThatNotExistsThrowDocumentNotFoundException() {
        when(clientRepository.findByNamesLike("ASDFG")).thenReturn(new ArrayList<Client>());
        Assertions.assertThrows(NotFoundException.class, () -> service.getClientsByNames("ASDFG"));
    }
    
    @Test
    public void GivenClientSurnameReturnListOfClients() {
        when(clientRepository.findBySurnamesLike("COFRE")).thenReturn(clientList);
        try {
            List<Client> newListCLientTest = this.service.getClientsBySurnames("COFRE");
            Assertions.assertEquals(clientList, newListCLientTest);
        } catch (Exception ex) {
            Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenSurnameThatNotExistsThrowDocumentNotFoundException() {
        when(clientRepository.findBySurnamesLike("ASDFG")).thenReturn(new ArrayList<Client>());
        Assertions.assertThrows(NotFoundException.class, () -> service.getClientsBySurnames("ASDFG"));
    }
    
    @Test
    public void GivenClientNameAndSurnameReturnListOfClients() {
        when(clientRepository.findByNamesIgnoringCaseLikeAndSurnamesIgnoringCaseLike("KAREN", "COFRE"))
                .thenReturn(clientList);
        try {
            List<Client> newListCLientTest = this.service.getClientByNamesAndSurnames("KAREN", "COFRE");
            Assertions.assertEquals(clientList, newListCLientTest);
        } catch (Exception ex) {
            Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenNameAndSurnameThatNotExistsThrowDocumentNotFoundException() {
        when(clientRepository.findByNamesIgnoringCaseLikeAndSurnamesIgnoringCaseLike("ASDFG", "ASDFG"))
                .thenReturn(new ArrayList<>());
        Assertions.assertThrows(NotFoundException.class, () -> service.getClientByNamesAndSurnames("ASDFG", "ASDFG"));
    }
    
    @Test
    public void GivenListOfBuroRsReturnListOfClients() {
        PersonaRs personaSample = new PersonaRs();
        PaisRs nacionalidadSample = new PaisRs();
        personaSample.setNacionalidad(nacionalidadSample);
        BuroRs buroRsSample1 = new BuroRs();
        buroRsSample1.setPersona(personaSample);
        BuroRs buroRsSample2 = new BuroRs();
        buroRsSample2.setPersona(personaSample);
        List<BuroRs> newListBuroRs = new ArrayList<>();
        newListBuroRs.add(buroRsSample1);
        newListBuroRs.add(buroRsSample2);
        Logger.getLogger(ClientServiceTests.class.getName()).log(Level.SEVERE, null, this.service.transformBuroRsToClient(newListBuroRs).toString());
        Assertions.assertEquals(clientList, this.service.transformBuroRsToClient(newListBuroRs));
    }
    
}
