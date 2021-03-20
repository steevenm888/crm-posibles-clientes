package ec.edu.espe.banquito.crm.posibles.clientes.repository;

import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {

    List<Client> findAll();

    Client findById();

    Optional<Client> findByIdentification(String identification);

    List<Client> findByNamesLike(String names);

    List<Client> findBySurnamesLike(String surnames);

    List<Client> findByNamesIgnoringCaseLikeAndSurnamesIgnoringCaseLike(String names, String surnames);

    List<Client> findByAmountOwedLessThanOrderByAmountOwedDesc(BigDecimal amountOwed);

    List<Client> findByAlternateRatingBetweenOrderByAlternateRatingDesc(BigDecimal from, BigDecimal to);

}
