package com.pseudoorganization.pseudosystemsintegration.controllers.auth;



import com.pseudoorganization.pseudosystemsintegration.models.User;
import com.pseudoorganization.pseudosystemsintegration.rest.models.request.LoginRequest;
import com.pseudoorganization.pseudosystemsintegration.rest.models.response.LoginResponse;
import com.pseudoorganization.pseudosystemsintegration.security.AuthProvider;
import com.pseudoorganization.pseudosystemsintegration.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {
    AuthProvider authProvider;
    JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthProvider authProvider, JwtTokenUtil jwtTokenUtil) {
        this.authProvider = authProvider;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("Login request: " + loginRequest);
            Authentication authentication;
            authentication = authProvider.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            User user = (User) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(user);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);

            return ResponseEntity.ok().body(loginResponse);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
