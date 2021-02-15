/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.model;

import java.util.List;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author esteban
 */

@Data
@Builder
@Document(collection = "client")
public class Client {
    @Id
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
    private String contributor;
    
}
