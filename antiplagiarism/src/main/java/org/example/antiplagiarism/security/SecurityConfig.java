package org.example.antiplagiarism.security;

import org.example.antiplagiarism.entity.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(securityContextRepository)
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**", "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()

//                        .requestMatchers(HttpMethod.GET, "/api/grading/**", "/api/submissions/**")
//                        .authenticated()
//
//                        .requestMatchers(HttpMethod.POST, "/api/submissions/**")
//                        .hasRole(UserRole.STUDENT.name())
//
//                        .requestMatchers(HttpMethod.PUT, "/api/grading/**")
//                        .hasRole(UserRole.REVIEWER.name())

                        .anyRequest()
//                        .hasRole(UserRole.ADMIN.name())
                        .authenticated()

                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

}