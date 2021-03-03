/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.model;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
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
    private String nationality;
    private String rating;
    private BigDecimal amountOwed;
    private BigDecimal alternateRating;
}
