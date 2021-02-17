/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.enums;

/**
 *
 * @author esteban
 */
public enum GenreEnum {
    MASCULINO("MAS", "MASCULINO"),
    FEMENINO("FEM", "FEMENINO");
    
    private final String code;
    private final String description;
    
    private GenreEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}
