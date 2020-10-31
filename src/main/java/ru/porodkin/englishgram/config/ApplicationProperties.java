package ru.porodkin.englishgram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreInvalidFields = true)
public class ApplicationProperties {
}
