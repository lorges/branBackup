package pl.com.peterpan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.com.peterpan.model.Role;
import pl.com.peterpan.model.User;
import pl.com.peterpan.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        User user = repository.findByLoginName(loginName)
                .orElseThrow(() -> new RuntimeException("User not found: " + loginName));
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for(Role role: user.getRoleSet()) {
            authorityList.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getLoginName(), user.getPassword(), authorityList);
    }
}
