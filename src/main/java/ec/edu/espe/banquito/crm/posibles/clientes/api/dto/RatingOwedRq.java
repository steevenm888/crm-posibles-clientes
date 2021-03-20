package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingOwedRq {

    String rating;
    BigDecimal amountOwed;
}
