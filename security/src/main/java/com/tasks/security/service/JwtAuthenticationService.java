package com.tasks.security.service;

import com.tasks.domain.exception.NotFoundException;
import com.tasks.security.dto.JwtAuthenticatoinDTO;
import com.tasks.domain.dto.UserDTO;
import com.tasks.security.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    public JwtAuthenticationService(AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService,
                                    JwtTokenUtils jwtTokenUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userService = userService;
    }

    public UserDTO generateToken(JwtAuthenticatoinDTO jwtAuthenticatoinDTO) throws Exception {

        log.info("Gerando um novo token");

        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthenticatoinDTO.getEmail(), jwtAuthenticatoinDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthenticatoinDTO.getEmail());

        String token = this.jwtTokenUtils.obterToken(userDetails);

        if (StringUtils.isEmpty(token)) {
            throw new Exception("Cannot generate valid token!");
        }

        return UserDTO.of(token, this.userService.loadUserByEmail(jwtAuthenticatoinDTO.getEmail()));
    }

    public UserDTO refreshToken(String token, String userEmail) throws NotFoundException {

        if (!StringUtils.isEmpty(token) && token.startsWith(BEARER_PREFIX)) {
            token.substring(7);
        }

        if(StringUtils.isEmpty(token)) {
            throw new NotFoundException("Token não informado!");
        }

        if(!jwtTokenUtils.tokenValido(token)) {
            throw new NotFoundException("Token inválido!");
        }

        token = jwtTokenUtils.refreshToken(token);

        return UserDTO.of(token, this.userService.loadUserByEmail(userEmail));
    }
}
