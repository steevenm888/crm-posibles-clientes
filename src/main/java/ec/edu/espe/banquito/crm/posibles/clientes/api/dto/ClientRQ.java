/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.api.dto;

import java.util.List;
import lombok.Data;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Phone;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Address;
import java.util.Date;

/**
 *
 * @author esteban
 */

@Data
public class ClientRQ {
    private String id;
    private String identification;
    private String names;
    private String surnames;
    private String genre;
    private String birthdate;
    private List<Phone> phones;
    private List<Address> addresses;
    private String email;
    private String nationality;
}
