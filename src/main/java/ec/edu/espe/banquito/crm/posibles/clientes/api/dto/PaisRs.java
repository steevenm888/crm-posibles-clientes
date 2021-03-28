package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaisRs {

    String id;
    String nombre;
    String codAlterno;
}
