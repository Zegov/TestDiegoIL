package com.sermaluc.prueba.apiuser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sermaluc.prueba.apiuser.security.TokenAccess;


@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    private TokenAccess tokenFilter;

    public SecurityConfig(TokenAccess tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenFilter);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
