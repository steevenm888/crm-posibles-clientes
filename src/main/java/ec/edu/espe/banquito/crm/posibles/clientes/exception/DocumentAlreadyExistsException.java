/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.exception;

/**
 *
 * @author esteban
 */
public class DocumentAlreadyExistsException extends RuntimeException {
    public DocumentAlreadyExistsException(String msg) {
        super(msg);
    }
}