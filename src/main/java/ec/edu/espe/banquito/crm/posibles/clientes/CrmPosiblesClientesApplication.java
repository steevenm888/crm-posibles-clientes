package ec.edu.espe.banquito.crm.posibles.clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan("com.mongotest") //to scan packages mentioned
@EnableMongoRepositories("com.mongotest") //to activate MongoDB repositories
public class CrmPosiblesClientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmPosiblesClientesApplication.class, args);
	}

}
