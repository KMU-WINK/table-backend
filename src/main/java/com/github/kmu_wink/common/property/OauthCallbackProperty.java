package com.github.kmu_wink.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.oauth.callback")
public class OauthCallbackProperty {

	@NotBlank
	private String success;

	@NotBlank
	private String failure;
}