/*
 * Creation date: 1 mar. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Alan Quimbita
 */
@Builder
@Data
public class PersonaRS {

    private String cedula;

    private String nombres;

    private String apellidos;

    private String nombreCompleto;

    private String genero;

    private String fechaNacimiento;

    private PaisRS nacionalidad;
}
