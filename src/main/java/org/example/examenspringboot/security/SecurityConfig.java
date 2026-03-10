package org.example.examenspringboot.security;

import jakarta.servlet.http.HttpServletResponse;
import org.example.examenspringboot.dto.ErrorResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable() )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.GET,"/**").permitAll() // Permitir todos los GET
                                .requestMatchers(HttpMethod.POST,"/**").authenticated() // Autenticar todos los POST
                                .requestMatchers(HttpMethod.PUT,"/**").authenticated() // Autenticar todos los PUT
//                                .requestMatchers(HttpMethod.DELETE,"/**").authenticated() // Autenticar todos los DELETE
                                .anyRequest().permitAll() // todo el resto lo permite
                ).httpBasic(basic -> {
                    basic.authenticationEntryPoint(customAuthenticationEntryPoint());
                });
        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return ((request, response, authException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ErrorResponseDTO error = new ErrorResponseDTO(LocalDateTime.now(),401,"Usuario no autorizado", "El usuario y la contraseña no coinciden");
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(error));
        });
    }
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin")
                .password("{noop}admin123") // noop para no encriptar la contraseña
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
