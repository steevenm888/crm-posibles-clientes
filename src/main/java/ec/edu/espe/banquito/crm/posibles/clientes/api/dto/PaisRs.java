package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaisRs {

    String id;
    String nombre;
    String codAlterno;
}
