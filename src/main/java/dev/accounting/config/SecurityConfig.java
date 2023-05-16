package dev.accounting.config;

import dev.accounting.security.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс конфигурации для Spring Security
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PersonDetailsService personDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html").permitAll()
                .antMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/api/v1/passports/create", "/api/v1/passports/{number}/update", "/api/v1/passports/{number}/delete").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers("/api/v1/model/stations/create", "/api/v1/model/stations/{id}/update", "/api/v1/model/stations/{id}/delete", "/api/v1/model/pathways/create", "/api/v1/model/pathways/{id}/delete").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers("/api/v1/cargos/create", "/api/v1/cargos/{id}/edit", "/api/v1/cargos/{id}/delete").hasAnyRole("MODERATOR", "ADMIN")
                .antMatchers("/api/v1/documents/create", "/api/v1/documents/reorder", "/api/v1/documents/departure/{pathwayId}/pathway", "/api/v1/documents/{id}/delete").hasAnyRole("MODERATOR", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * Настройка шифрования
     * @return объект шифрования
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
