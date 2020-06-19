package com.tasks;

import com.tasks.security.enums.RoleAccess;
import com.tasks.security.model.User;
import com.tasks.security.repository.UserRepository;
import com.tasks.security.utils.SenhaUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"com.tasks.domain.repository", "com.tasks.security.repository"})
@SpringBootApplication
public class TasksAppApplication {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(TasksAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {

            User user = new User();
            user.setEmail("jardel@email.com");
            user.setPassword(SenhaUtils.gerarBCrypt("123456"));
            user.setRole(RoleAccess.ROLE_ADMIN);

            user = this.userRepository.save(user);
        };
    }
}
