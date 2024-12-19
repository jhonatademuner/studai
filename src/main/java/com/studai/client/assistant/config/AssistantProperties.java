package com.studai.client.assistant.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "assistant")
public class AssistantProperties {

    private String baseUri;
    private String openaiApiKey;

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public void setOpenaiApiKey(String openaiApiKey) {
        this.openaiApiKey = openaiApiKey;
    }
}
