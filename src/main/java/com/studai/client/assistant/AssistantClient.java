package com.studai.client.assistant;

import com.studai.client.assistant.config.AssistantProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class AssistantClient {

    private final RestTemplate restTemplate;
    private final AssistantProperties assistantProperties;

    @Autowired
    public AssistantClient(RestTemplate restTemplate, AssistantProperties assistantProperties) {
        this.restTemplate = restTemplate;
        this.assistantProperties = assistantProperties;
    }

    public <T> T postRequest(String endpoint, Object body, Map<String, String> headers, Map<String, String> queryParams, Class<T> responseClass) {
        try {
            URI uri = buildUri(endpoint, queryParams);
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<Object> requestEntity = new HttpEntity<>(body, httpHeaders);

            ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseClass);
            if (response.getStatusCode().is2xxSuccessful()) {
                T responseBody = response.getBody();
                if (responseBody != null) {
                    return responseBody;
                } else {
                    throw new RuntimeException("Empty response body for " + endpoint);
                }
            } else {
                throw new RuntimeException("Failed request with status code: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            System.err.println("Error during POST request to " + endpoint + ": " + e.getMessage());
            throw new RuntimeException("Error during POST request to " + endpoint, e);
        }
    }

    private URI buildUri(String endpoint, Map<String, String> queryParams) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(assistantProperties.getBaseUri() + endpoint);

        if (queryParams != null) {
            queryParams.forEach(uriBuilder::queryParam);
        }

        return uriBuilder.build().toUri();
    }

    private HttpHeaders createHeaders(Map<String, String> additionalHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + assistantProperties.getOpenaiApiKey());

        if (additionalHeaders != null) {
            additionalHeaders.forEach(headers::set);
        }

        return headers;
    }
}
