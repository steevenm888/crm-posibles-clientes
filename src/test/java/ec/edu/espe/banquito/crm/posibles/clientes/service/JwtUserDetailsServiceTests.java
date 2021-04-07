package ec.edu.espe.banquito.crm.posibles.clientes.service;

import ec.edu.espe.banquito.crm.posibles.clientes.model.Client;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JwtUserDetailsServiceTests {
    
    @InjectMocks
    private JwtUserDetailsService service;
    
    @Test
    public void GivenUsernameReturnUser() {
        User userSample = new User("user-crm",
                    "$2a$10$aDDRu4KfnWgWmNt3QfHsoutJBlGpemM/OlcmrN9n4pK25zU5LL0NW",
                    new ArrayList<>());
        Assertions.assertEquals(userSample, service.loadUserByUsername("user-crm"));
    }
    
    @Test
    public void GivenUsernameThatNotExistsThrowUserNotFoundException() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("admin"));
    }
    
}
