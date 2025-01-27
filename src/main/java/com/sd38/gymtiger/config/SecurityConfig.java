/*
 *  © 2023 Nyaruko166
 */

package com.sd38.gymtiger.config;

import com.sd38.gymtiger.security.CustomAccessDeniedHandler;
import com.sd38.gymtiger.security.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService getDetailsService() {
        return new CustomUserDetailService();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/home").loginProcessingUrl("/login").permitAll()).logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());

        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(req -> {
            //Allowing Resources
            req.requestMatchers("/*.css", "/*.js", "/assets/**", "/admin/**", "/user/**").permitAll();

            //Testing purpose
            req.requestMatchers("/login", "/register", "/mail").permitAll();

            //Role base authority
            req.requestMatchers("/tiger/pos/**", "/tiger/bill/**").hasAnyAuthority("Admin", "Employee")
                    .requestMatchers("/tiger/**").hasAuthority("Admin")
                    .requestMatchers("/profile").authenticated().anyRequest().permitAll();
        });

        http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()));

        return http.build();
    }
}
