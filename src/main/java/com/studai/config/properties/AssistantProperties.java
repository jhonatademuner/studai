package com.studai.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "assistant")
public class AssistantProperties {

    private String baseUri;
    private String openaiApiKey;

}
