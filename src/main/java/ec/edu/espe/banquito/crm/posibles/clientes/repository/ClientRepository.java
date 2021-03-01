/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.posibles.clientes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author esteban
 */
public interface ClientRepository extends MongoRepository<Client, String> {

    List<Client> findAll();

    Client findById();

    Optional<Client> findByIdentification(String identification);

    List<Client> findByNamesLike(String names);

    List<Client> findBySurnamesLike(String Surnames);

    List<Client> findByNamesIgnoringCaseLikeAndSurnamesIgnoringCaseLike(String names, String surnames);

    List<Client> findByAmountOwedLessThanOrderByAmountOwedDesc(BigDecimal amountOwed);

    List<Client> findByAlternateRatingBetweenOrderByAlternateRatingDesc(BigDecimal from, BigDecimal to);

}
