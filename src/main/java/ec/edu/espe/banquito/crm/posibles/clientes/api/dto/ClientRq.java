package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import ec.edu.espe.banquito.crm.posibles.clientes.model.Address;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Phone;
import java.util.List;
import lombok.Data;

@Data
public class ClientRq {
    private String id;
    private String identification;
    private String names;
    private String surnames;
    private String genre;
    private String birthdate;
    private List<Phone> phones;
    private List<Address> addresses;
    private String email;
    private String nationality;
}
