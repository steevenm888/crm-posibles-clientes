/*
 * Creation date: 3 mar. 2021
 * Company: ESPE
 * Project: Exámen Segundo Parcial
 */
package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;


import java.math.BigDecimal;
import lombok.Data;
import lombok.Builder;

/**
 *
 * @author Alan Quimbita
 */
@Data
@Builder
public class RatingOwedRQ {

    String rating;
    BigDecimal amountOwed;
}
