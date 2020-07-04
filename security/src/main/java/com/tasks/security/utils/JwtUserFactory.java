package com.tasks.security.utils;

import com.tasks.domain.enums.RoleAccess;
import com.tasks.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {

    /**
     * Converte e gera um Jwtuser com base nos dados do usuario
     *
     * @param user
     * @return
     */
    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getEmail(), user.getPassword(), mapToGrantedAuthorities(user.getRole()));
    }

    /**
     * Converte o perfil do usuário para o formato utilizado pelo Spring Security.
     *
     * @param roleAccess
     * @return List<GrantedAuthority>
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(RoleAccess role) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        return authorities;
    }
}
