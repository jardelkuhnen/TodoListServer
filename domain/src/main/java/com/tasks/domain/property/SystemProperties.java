package com.tasks.domain.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "system.properties")
public class SystemProperties {

    @NotBlank
    private String urlsystem;

}
