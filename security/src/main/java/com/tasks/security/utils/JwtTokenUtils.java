package com.tasks.security.utils;

import com.tasks.security.configuration.AuthTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenUtils {

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_ROLE = "role";
    static final String CLAIM_KEY_CREATED = "created";
    private final AuthTokenProperties authTokenProperties;

    @Autowired
    public JwtTokenUtils(AuthTokenProperties authTokenProperties) {
        this.authTokenProperties = authTokenProperties;
    }

    public AuthTokenProperties getAuthTokenProperties() {
        return authTokenProperties;
    }

    /**
     * Obtém o username (email) contido no token JWT.
     *
     * @param token
     * @return String
     */
    public String getUsernameFromToken(String token) {
        String username;

        try {

            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();

        } catch (Exception e) {
            username = null;
        }

        return username;
    }

    /**
     * Cria um novo token (refresh).
     *
     * @param token
     * @return String
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = gerarToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * Verifica e retorna se um token JWT é válido.
     *
     * @param token
     * @return boolean
     */
    public boolean tokenValido(String token) {
        return !tokenExpirado(token);
    }

    /**
     * Retorna um novo token JWT com base nos dados do usuários.
     *
     * @param userDetails
     * @return String
     */
    public String obterToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        userDetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
        claims.put(CLAIM_KEY_CREATED, new Date());

        return gerarToken(claims);
    }

    /**
     * Retorna a data de expiração de um token JWT.
     *
     * @param token
     * @return Date
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * Realiza o parse do token JWT para extrair as informações contidas no corpo
     * dele.
     *
     * @param token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;

        try {
            claims = Jwts.parser().setSigningKey(this.authTokenProperties.getSecret()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * Retorna a data de expiração com base na data atual.
     *
     * @return Date
     */
    private Date generateExpirationDate() {
        BigDecimal multiply = this.authTokenProperties.getExpiration().multiply(new BigDecimal(1000));

        return new Date(System.currentTimeMillis() + multiply.longValue());
    }

    /**
     * Verifica se um token JTW está expirado.
     *
     * @param token
     * @return boolean
     */
    private boolean tokenExpirado(String token) {
        Date dataExpiracao = this.getExpirationDateFromToken(token);

        if (dataExpiracao == null) {
            return false;
        }

        return dataExpiracao.before(new Date());
    }

    /**
     * Gera um novo token JWT contendo os dados (claims) fornecidos.
     *
     * @param claims
     * @return String
     */
    private String gerarToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate()).setIssuedAt(generateCurrentDate())
                .signWith(SignatureAlgorithm.HS512, this.authTokenProperties.getSecret()).compact();
    }

}


