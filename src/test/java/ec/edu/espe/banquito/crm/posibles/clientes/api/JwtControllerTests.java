package ec.edu.espe.banquito.crm.posibles.clientes.api;

import ec.edu.espe.banquito.crm.posibles.clientes.auth.TokenManager;
import ec.edu.espe.banquito.crm.posibles.clientes.model.JwtRequestModel;
import ec.edu.espe.banquito.crm.posibles.clientes.model.JwtResponseModel;
import ec.edu.espe.banquito.crm.posibles.clientes.service.JwtUserDetailsService;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class JwtControllerTests {
    
    @Mock
    private JwtUserDetailsService service;
    private AuthenticationManager authManager;
    private TokenManager tokenManager;
    
    @InjectMocks
    private JwtController controller;
    
    @Test
    public void GivenUsernameAndPasswordReturnResponseEntityOkWithToken() {
        JwtRequestModel request = new JwtRequestModel();
        request.setUsername("user-crm-possible-clients");
        request.setPassword("espe123.");
        try {
            Assertions.assertEquals(ResponseEntity.ok(new JwtResponseModel("1234567890abcdefg")), controller.createToken(request));
        } catch (Exception ex) {
            Logger.getLogger(JwtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenDisabledUserReturnResponseEntityForbidden() {
        JwtRequestModel request = new JwtRequestModel();
        request.setUsername("user-crm-possible-clients");
        request.setPassword("espe123.");
        when(authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            )).thenThrow(new DisabledException(""));
        try {
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.FORBIDDEN).build(), controller.createToken(request));
        } catch (Exception ex) {
            Logger.getLogger(JwtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenWrongUsernameOrPasswordReturnResponseEntityBadRequest() {
        JwtRequestModel request = new JwtRequestModel();
        request.setUsername("user-crm-possible-clients");
        request.setPassword("espe123.");
        when(authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            )).thenThrow(new BadCredentialsException(""));
        try {
            Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.createToken(request));
        } catch (Exception ex) {
            Logger.getLogger(JwtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
