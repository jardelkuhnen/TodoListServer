package com.tasks.security.service;

import com.tasks.security.dto.JwtAuthenticatoinDTO;
import com.tasks.security.utils.JwtTokenUtils;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;

    @Autowired
    public JwtAuthenticationService(AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService,
                                    JwtTokenUtils jwtTokenUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public String generateToken(JwtAuthenticatoinDTO jwtAuthenticatoinDTO) throws Exception {

        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthenticatoinDTO.getEmail(), jwtAuthenticatoinDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthenticatoinDTO.getEmail());

        String token = this.jwtTokenUtils.obterToken(userDetails);

        if(StringUtils.isEmpty(token)) {
            throw new Exception("Cannot generate valid token!");
        }

        return token;
    }
}
