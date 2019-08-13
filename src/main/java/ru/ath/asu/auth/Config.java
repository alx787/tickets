package ru.ath.asu.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
//import org.springframework.context.annotation.ScopedProxyMode;
//import org.springframework.web.context.WebApplicationContext;

import javax.inject.Named;

@Named
@Configuration
public class Config {
    @Bean(name = "userInfo")
    @Scope("session")
//    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    @Scope(value = WebApplicationContext.SCOPE_SESSION)
    public UserInfo getSessionUserInfo() {
        return new UserInfo();
    }
}
