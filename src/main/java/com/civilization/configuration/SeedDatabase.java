package com.civilization.configuration;

import com.civilization.model.User;
import com.civilization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SeedDatabase {
    @Autowired
    private UserService userService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAutoValue;

    @PostConstruct
    public void databaseSeed() {
        if ("create".equals(ddlAutoValue)) {
            userService.save(new User("Fene4ka_(Женя)", 1000L));
            userService.save(new User("gigasvin323", 900L));
        }
    }

}
