package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuroRs {

    private PersonaRs persona;

    private String calificacion;

    private BigDecimal cantidadAdeudada;

    private BigDecimal calificacionAlterna;

}
