package io.alexandru.ppmtool.beans;

import io.alexandru.ppmtool.repositories.*;
import io.alexandru.ppmtool.services.EmailService;
import io.alexandru.ppmtool.services.ProjectService;
import io.alexandru.ppmtool.services.ProjectTaskService;
import io.alexandru.ppmtool.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ComponentScan(basePackages = "io.alexandru.ppmtool")
public class Config {

    @Bean
    public UserService userService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                                   PasswordResetTokenRepository tokenRepository, EmailService emailService) {
        return new UserService(userRepository, bCryptPasswordEncoder, tokenRepository, emailService);
    }

    @Bean
    public ProjectTaskService projectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository,
                                                 ProjectRepository projectRepository, ProjectService projectService) {
        return new ProjectTaskService(backlogRepository, projectTaskRepository, projectRepository, projectService);
    }

    @Bean
    public ProjectService projectService(ProjectRepository projectRepository, BacklogRepository backlogRepository, UserRepository userRepository) {
        return new ProjectService(projectRepository, backlogRepository, userRepository);
    }

}
