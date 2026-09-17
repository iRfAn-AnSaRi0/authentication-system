package com.authentication.authentication_system.config;

import com.authentication.authentication_system.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http
                 .csrf(csrf->csrf.disable())
                 .cors(cors->{})
                 .headers(headers -> headers
                         .frameOptions(frame -> frame.disable())
                 )
                 .sessionManagement(session ->
                         session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authorizeHttpRequests(auth->auth
                         .requestMatchers(
                                 "/api/v1/register",
                                 "/api/v1/login",
                                 "/api/v1/logout",
                                 "/api/v1/refresh"
                         ).permitAll()
                         .requestMatchers("/h2-console/**").permitAll()
                         .anyRequest().authenticated()
                 )
                 .addFilterBefore(
                         jwtAuthenticationFilter,
                         UsernamePasswordAuthenticationFilter.class
                 );

         return http.build();
    }

}
