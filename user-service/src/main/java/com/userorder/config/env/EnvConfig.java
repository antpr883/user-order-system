package com.userorder.config.env;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EnvConfig {

    @Bean
    @Profile("!test")
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(ConfigurableEnvironment environment) {

        String defaultEnvDir=".";

        String activeProfile = environment.getActiveProfiles().length > 0
                ? environment.getActiveProfiles()[0]
                : "default";

        String envDirectory;
        switch (activeProfile) {
            case "docker":
                envDirectory = "docker";
                break;
            case "development":
                envDirectory = defaultEnvDir;
                break;
            default:
                envDirectory = defaultEnvDir;
                break;
        }

        // Завантажуємо відповідний dev.env
        Dotenv dotenv = Dotenv.configure()
                .directory(envDirectory)
                .filename("dev.env")
                .ignoreIfMissing()
                .load();

        // Convert to properties map
        Map<String, Object> envProperties = new HashMap<>();
        dotenv.entries().forEach(entry -> envProperties.put(entry.getKey(), entry.getValue()));

        // Add as property source with highest precedence
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("dotenv", envProperties));

        // Return configurer
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setEnvironment(environment);
        return configurer;
    }
}