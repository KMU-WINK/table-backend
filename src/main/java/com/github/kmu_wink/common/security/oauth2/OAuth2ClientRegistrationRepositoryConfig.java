package com.github.kmu_wink.common.security.oauth2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.ClientsConfiguredCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Conditional(ClientsConfiguredCondition.class)
public class OAuth2ClientRegistrationRepositoryConfig {

    private final OAuth2ClientProperties properties;

    @Bean
    @ConditionalOnMissingBean(ClientRegistrationRepository.class)
    public InMemoryClientRegistrationRepository clientRegistrationRepository() {

        return new InMemoryClientRegistrationRepository(
            new OAuth2ClientPropertiesMapper(properties)
                .asClientRegistrations()
                .values()
                .stream()
                .toList()
        );
    }
}