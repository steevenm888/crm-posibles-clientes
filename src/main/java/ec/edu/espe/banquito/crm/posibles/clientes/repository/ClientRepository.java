/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import java.util.List;

/**
 *
 * @author esteban
 */
public interface ClientRepository extends MongoRepository<Client, String>{
    List<Client> findAll();
    
    Client findById();
    
}
