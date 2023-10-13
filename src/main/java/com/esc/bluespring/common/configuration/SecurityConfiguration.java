package com.esc.bluespring.common.configuration;

import com.esc.bluespring.common.security.AuthenticationEntryPointImpl;
import com.esc.bluespring.common.security.JwtAuthenticationConverter;
import com.esc.bluespring.domain.member.entity.Member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationConverter converter;
    private final JwtDecoder jwtDecoder;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPointImpl restAuthenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(
            httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                corsConfigurationSource)).authorizeHttpRequests(
            registry -> registry.requestMatchers("/swagger-ui/**").hasRole(Role.ADMIN.name())
                .anyRequest().permitAll()).oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(converter)
                    .decoder(jwtDecoder)).accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(restAuthenticationEntryPoint)).formLogin(
            form -> form.permitAll().usernameParameter("email").passwordParameter("password"));
        return http.build();
    }

    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder)
        throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return builder.build();
    }

    @Bean
    public ProviderManager authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

}