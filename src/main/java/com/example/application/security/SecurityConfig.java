package com.example.application.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/login/**",
                                "/logout",
                                "/frontend/**",
                                "/VAADIN/**",
                                "/webjars/**",
                                "/icons/**",
                                "/images/**",
                                "/styles/**",
                                "/manifest.webmanifest",
                                "/sw.js",
                                "/offline-page.html",
                                "/"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/"), // UIDL-pyynnöt tulevat tänne POSTilla
                                new AntPathRequestMatcher("/VAADIN/**"),
                                new AntPathRequestMatcher("/frontend/**")
                        )
                );
        setLoginView(http, "/login");
    }
}