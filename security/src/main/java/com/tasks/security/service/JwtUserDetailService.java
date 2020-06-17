package com.tasks.security.service;

import com.tasks.domain.model.User;
import com.tasks.security.repository.UserRepository;
import com.tasks.security.utils.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(userName);

        if(user != null) {
            return JwtUserFactory.create(user);
        }

        throw new UsernameNotFoundException("Email not found!");
    }
}
