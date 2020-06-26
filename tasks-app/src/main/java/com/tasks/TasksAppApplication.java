package com.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"com.tasks.domain.repository", "com.tasks.security.repository"})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TasksAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasksAppApplication.class, args);
    }
}
