package ec.edu.espe.banquito.crm.posibles.clientes.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "client")
@NoArgsConstructor
@AllArgsConstructor
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
