package com.tasks.emailsender.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "emailsender.config")
public class EmailProperties {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
