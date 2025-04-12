package com.github.kmu_wink.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.kmu_wink.common.security.jwt.TokenAuthenticationFilter;
import com.github.kmu_wink.common.security.jwt.TokenProvider;
import com.github.kmu_wink.common.security.oauth2.CustomOAuth2UserService;
import com.github.kmu_wink.common.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.github.kmu_wink.common.security.oauth2.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {

        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((httpRequests) ->
                httpRequests
                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization
                    .baseUri("/oauth2/authorization")
                )
                .redirectionEndpoint(redirection -> redirection
                    .baseUri("/*/oauth2/code/*")
                )
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
            );

        return httpSecurity.build();
    }
}
