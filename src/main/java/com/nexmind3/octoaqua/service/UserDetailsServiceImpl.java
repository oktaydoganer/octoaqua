package com.nexmind3.octoaqua.service;


import java.util.ArrayList;
import java.util.List;


import com.nexmind3.octoaqua.entity.Role;
import com.nexmind3.octoaqua.entity.User;
import com.nexmind3.octoaqua.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException(email);
        }
        if(!user.isAccountConfirmed()){
            throw new UsernameNotFoundException("Your account is not confirmed.");
        }
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (Role role: user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getAuthorities().stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::add);
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(), authorities);
    }
}