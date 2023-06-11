package com.pseudoorganization.pseudosystemsintegration.controllers.auth;


import com.pseudoorganization.pseudosystemsintegration.controllers.auth.request.LoginRequest;
import com.pseudoorganization.pseudosystemsintegration.controllers.auth.request.RegisterRequest;
import com.pseudoorganization.pseudosystemsintegration.controllers.auth.response.LoginResponse;
import com.pseudoorganization.pseudosystemsintegration.errors.ApplicationException;
import com.pseudoorganization.pseudosystemsintegration.errors.ErrorCodes;
import com.pseudoorganization.pseudosystemsintegration.models.User;
import com.pseudoorganization.pseudosystemsintegration.security.AuthProvider;
import com.pseudoorganization.pseudosystemsintegration.security.JwtTokenUtil;
import com.pseudoorganization.pseudosystemsintegration.security.SecurityUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin()
@RestController
public class AuthController {
    private final AuthProvider authProvider;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUserDetailsService securityUserDetailsService;

    public AuthController(AuthProvider authProvider, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, SecurityUserDetailsService securityUserDetailsService) {
        this.authProvider = authProvider;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.securityUserDetailsService = securityUserDetailsService;
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

    @PostMapping(value = "/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        if (!validateUsername(username) || StringUtils.isEmpty(registerRequest.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        String password = passwordEncoder.encode(registerRequest.getPassword());

        try {
            User user = new User();

            user.setUsername(username);
            user.setPassword(password);
            securityUserDetailsService.saveUser(user);
            return ResponseEntity.ok("User created");
        } catch (ApplicationException e) {
            if (e.getErrorCode() == ErrorCodes.USER_ALREADY_EXISTS) {
                return ResponseEntity.status(409).body("User already exists");
            }
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    public boolean validateUsername(String username) {
        String usernameRegex = "^[a-zA-Z0-9]{3,}$";
        return username.matches(usernameRegex);
    }

}
