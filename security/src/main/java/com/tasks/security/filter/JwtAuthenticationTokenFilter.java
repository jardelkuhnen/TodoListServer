package com.tasks.security.filter;

import com.tasks.domain.service.SystemUtilsService;
import com.tasks.security.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private SystemUtilsService systemUtilsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        log.info("Executando doFilterInternal ");

        String token = request.getHeader(AUTH_HEADER);

        if (!StringUtils.isEmpty(token) && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(7);
        }

        String userName = jwtTokenUtils.getUsernameFromToken(token);

        /**
         * Caso encontrado usuário e não estiver authenticado realiza a authentication
         */
        if (!StringUtils.isEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {


            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            /**
             * Caso token valido, nao expirado ou sem data de expiration devido não estar authenticado ainda
             */
            if (jwtTokenUtils.tokenValido(token)) {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                this.systemUtilsService.registerUser(userDetails.getUsername());

            }

        }

        chain.doFilter(request, response);
    }
}
