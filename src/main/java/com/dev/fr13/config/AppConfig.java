package com.dev.fr13.config;

import com.dev.fr13.skype.SkypeCredential;
import com.fasterxml.jackson.core.JsonProcessingException;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AppConfig {
    @Value("${skype.app-id}")
    private String skypeAppId;

    @Value("${skype.password}")
    private String skypePassword;

    @Bean
    public SkypeCredential skypeConnectionService() {
        return new SkypeCredential(skypeAppId, skypePassword);
    }

    @PostConstruct
    public void initUnirest() {
        Unirest.config().setObjectMapper(new ObjectMapper() {
            final com.fasterxml.jackson.databind.ObjectMapper mapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            @Override
            public String writeValue(Object value) {
                try {
                    return mapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return mapper.readValue(value, valueType);
                } catch (JsonProcessingException e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }
}
