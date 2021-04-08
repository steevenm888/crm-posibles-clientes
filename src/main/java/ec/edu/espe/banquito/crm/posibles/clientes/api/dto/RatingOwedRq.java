package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingOwedRq {

    String rating;
    BigDecimal amountOwed;
}
