package com.foreignexchange.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "foreign-exchange.external.client")
public class FastForexProperties {

    private String apiKey;
}
