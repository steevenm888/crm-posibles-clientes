package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaRs {

    private String cedula;

    private String nombres;

    private String apellidos;

    private String nombreCompleto;

    private String genero;

    private String fechaNacimiento;

    private PaisRs nacionalidad;
}
