package ru.ath.asu.tickets.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.inject.Named;

//@Named
@Configuration
public class AuthContextConfiguration {
    @Bean
    @Scope("session")
    public AuthUserInfo authUserInfo() {
        return new AuthUserInfoImpl();
    }
}
