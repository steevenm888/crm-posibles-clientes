package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PersonaRs {

    private String cedula;

    private String nombres;

    private String apellidos;

    private String nombreCompleto;

    private String genero;

    private String fechaNacimiento;

    private PaisRs nacionalidad;
}
