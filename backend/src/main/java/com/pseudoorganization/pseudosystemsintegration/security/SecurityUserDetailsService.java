package com.pseudoorganization.pseudosystemsintegration.security;


import com.pseudoorganization.pseudosystemsintegration.errors.ApplicationException;
import com.pseudoorganization.pseudosystemsintegration.errors.ErrorCodes;
import com.pseudoorganization.pseudosystemsintegration.models.User;
import com.pseudoorganization.pseudosystemsintegration.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public SecurityUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElse(null);
    }

    public void saveUser(User user) throws ApplicationException {
        if (loadUserByUsername(user.getUsername()) == null){
            userRepository.save(user);
        }else {
            throw new ApplicationException(ErrorCodes.USER_ALREADY_EXISTS);
        }

    }
}
