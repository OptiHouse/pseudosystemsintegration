package com.pseudoorganization.pseudosystemsintegration.security;


import com.pseudoorganization.pseudosystemsintegration.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {
    private final SecurityUserDetailsService securityUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthProvider(SecurityUserDetailsService securityUserDetailsService, PasswordEncoder passwordEncoder) {
        this.securityUserDetailsService = securityUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = (User) securityUserDetailsService.loadUserByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Niepoprawne hasło lub nazwa użytkownika");
        }

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("Niepoprawne hasło lub nazwa użytkownika");
        }else if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, password,  user.getAuthorities());
        }
        throw new AuthenticationServiceException("Nieznany błąd autoryzacji");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
