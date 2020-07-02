package com.tasks.security.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auth.security")
public class AuthTokenProperties {

    @NotBlank
    private String header = "Authorization";

    private BigDecimal expiration = new BigDecimal(3600);

    @NotBlank
    private String secret;

}
