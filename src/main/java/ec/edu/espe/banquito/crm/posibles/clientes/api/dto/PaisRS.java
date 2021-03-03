/*
 * Creation date: 1 mar. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Alan Quimbita
 */
@Builder
@Data
public class PaisRS {

    String id;
    String nombre;
    String codAlterno;
}
