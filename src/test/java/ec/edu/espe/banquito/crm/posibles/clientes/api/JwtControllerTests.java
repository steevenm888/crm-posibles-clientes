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
//    private AuthenticationManager authManager;
    private TokenManager tokenManager;
    
    @InjectMocks
    private JwtController controller;
    private AuthenticationManager authManager;
    
    @Test
    public void GivenUsernameAndPasswordReturnResponseEntityOkWithToken() {
        JwtRequestModel request = new JwtRequestModel("user-crm", "espe123.");
        request.setUsername("user-crm");
        request.setPassword("espe123.");
        JwtResponseModel response = new JwtResponseModel("1234567890abcdefg");
        Logger.getLogger(JwtController.class.getName()).log(Level.INFO, null, response.getToken());
        try {
            Assertions.assertEquals(ResponseEntity.ok(response), controller.createToken(request));
        } catch (Exception ex) {
            Logger.getLogger(JwtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenDisabledUserReturnResponseEntityForbidden() {
        JwtRequestModel request = new JwtRequestModel("user-crm", "espe123.");
        request.setUsername("user-crm");
        request.setPassword("espe123.");
        try {
            when(authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            )).thenThrow(new DisabledException(""));
        } catch (Exception ex) {
            Logger.getLogger(JwtController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Assertions.assertEquals(ResponseEntity.status(HttpStatus.FORBIDDEN).build(), controller.createToken(request));
        } catch (Exception ex) {
            Logger.getLogger(JwtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void GivenWrongUsernameOrPasswordReturnResponseEntityBadRequest() {
        JwtRequestModel request = new JwtRequestModel();
        request.setUsername("user-crm-");
        request.setPassword("espe123.");
        try {
            when(authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            )).thenThrow(new BadCredentialsException(""));
        } catch (Exception ex) {
            Logger.getLogger(JwtController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Assertions.assertEquals(ResponseEntity.badRequest().build(), controller.createToken(request));
        } catch (Exception ex) {
            Logger.getLogger(JwtController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
