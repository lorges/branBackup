package pl.com.peterpan.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.com.peterpan.model.User;
import pl.com.peterpan.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class DaoAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DaoAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> optUser = userRepository.findByLoginName(name);
        if(optUser.isPresent()) {
            User user = optUser.get();

            if(passwordEncoder.matches(password, user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
            }
        }
        throw new BadCredentialsException("Someone provided incorrect credentials.");
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
