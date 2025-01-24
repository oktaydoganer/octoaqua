package com.nexmind3.octoaqua.configuration;

import com.nexmind3.octoaqua.repository.GenericRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableJpaRepositories(basePackages = "com.nexmind3.octoaqua.repository", repositoryBaseClass = GenericRepositoryImpl.class)
@ComponentScan("com.nexmind3.octoaqua")
@EntityScan("com.nexmind3.octoaqua.*")
public class BaseConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
