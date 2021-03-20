package ec.edu.espe.banquito.crm.posibles.clientes.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationValues {

    private final String mongoHost;
    private final String mongoDb;

    @Autowired
    public ApplicationValues(
            @Value("${clients.mongo.host}") String mongoHost,
            @Value("${clients.mongo.db}") String mongoDb) {
        this.mongoHost = mongoHost;
        this.mongoDb = mongoDb;
    }

}
