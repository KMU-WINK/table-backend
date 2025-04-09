package com.github.kmu_wink.common.property;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = "com.github.kmu_wink.common.property")
public class PropertyConfig {
}
