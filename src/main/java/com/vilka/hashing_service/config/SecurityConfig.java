package com.vilka.hashing_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфігурація безпеки для додатка.
 */
@Configuration
public class SecurityConfig {

    /**
     * Налаштовує ланцюг безпеки для HTTP-запитів.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        // Налаштування доступу до різних ендпоінтів
        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers("/swagger-ui/**").permitAll() // Доступ до Swagger UI без авторизації
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/api/getHash").authenticated() // Доступ тільки для автентифікованих користувачів
                    .requestMatchers("/api/getPhoneNumber").authenticated()
                    .anyRequest().authenticated(); // Усі інші запити також потребують авторизації
        });

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Створює користувача для тестової автентифікації, збереженого в пам'яті.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
