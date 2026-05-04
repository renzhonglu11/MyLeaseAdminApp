package com.rz.lease.web.app.custom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rz.lease.common.result.Result;
import com.rz.lease.common.result.ResultCodeEnum;
import com.rz.lease.web.app.custom.filter.AppJwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final AppJwtAuthenticationFilter appJwtAuthenticationFilter;

    public AppSecurityConfig(AppJwtAuthenticationFilter appJwtAuthenticationFilter) {
        this.appJwtAuthenticationFilter = appJwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, ex) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Result.fail(ResultCodeEnum.APP_LOGIN_AUTH.getCode(),
                                            ResultCodeEnum.APP_LOGIN_AUTH.getMessage())));
                        })
                        .accessDeniedHandler((request, response, ex) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    Result.fail(ResultCodeEnum.APP_LOGIN_AUTH.getCode(),
                                            ResultCodeEnum.APP_LOGIN_AUTH.getMessage())));
                        }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/app/login",
                                "/app/login/getCode",
                                "/app/apartment/**",
                                "/app/room/**",
                                "/app/region/**",
                                "/app/payment/**",
                                "/app/term/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(appJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
