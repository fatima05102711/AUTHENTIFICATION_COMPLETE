package com.ogn.orange.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity
@Configuration
public class  ConfigurationSecuriteApplication {
    private final JwtAuthentificationFilter jwtAuthFilter;
    private final AuthenticationProvider authentificationProvider;
    public ConfigurationSecuriteApplication(JwtAuthentificationFilter jwtAuthFilter, AuthenticationProvider authentificationProvider)
    {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authentificationProvider = authentificationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/user/**").permitAll()
                .requestMatchers("/api/authentification/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html","/v3/api-docs/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authentificationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
