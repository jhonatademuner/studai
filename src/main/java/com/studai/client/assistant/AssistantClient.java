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

    /**
     * Makes a GET request to the specified endpoint with query parameters.
     *
     * @param endpoint      The endpoint relative to the base URI.
     * @param queryParams   Query parameters to append to the URI.
     * @param responseClass Expected response type.
     * @return The response body.
     */
    public <T> T getRequest(String endpoint, Map<String, String> queryParams, Class<T> responseClass) {
        try {
            URI uri = buildUri(endpoint, queryParams);
            ResponseEntity<T> response = restTemplate.getForEntity(uri, responseClass);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Error during GET request to " + endpoint, e);
        }
    }

    /**
     * Makes a POST request to the specified endpoint with an optional body and query parameters.
     *
     * @param endpoint      The endpoint relative to the base URI.
     * @param body          Request body (can be null).
     * @param headers       Additional headers (can be null).
     * @param queryParams   Query parameters to append to the URI.
     * @param responseClass Expected response type.
     * @return The response body.
     */
    public <T> T postRequest(String endpoint, Object body, Map<String, String> headers, Map<String, String> queryParams, Class<T> responseClass) {
        return exchangeRequest(HttpMethod.POST, endpoint, body, headers, queryParams, responseClass);
    }

    /**
     * Makes a PUT request to the specified endpoint with an optional body and query parameters.
     *
     * @param endpoint      The endpoint relative to the base URI.
     * @param body          Request body (can be null).
     * @param headers       Additional headers (can be null).
     * @param queryParams   Query parameters to append to the URI.
     * @param responseClass Expected response type.
     * @return The response body.
     */
    public <T> T putRequest(String endpoint, Object body, Map<String, String> headers, Map<String, String> queryParams, Class<T> responseClass) {
        return exchangeRequest(HttpMethod.PUT, endpoint, body, headers, queryParams, responseClass);
    }

    /**
     * Makes a PATCH request to the specified endpoint with an optional body and query parameters.
     *
     * @param endpoint      The endpoint relative to the base URI.
     * @param body          Request body (can be null).
     * @param headers       Additional headers (can be null).
     * @param queryParams   Query parameters to append to the URI.
     * @param responseClass Expected response type.
     * @return The response body.
     */
    public <T> T patchRequest(String endpoint, Object body, Map<String, String> headers, Map<String, String> queryParams, Class<T> responseClass) {
        return exchangeRequest(HttpMethod.PATCH, endpoint, body, headers, queryParams, responseClass);
    }

    /**
     * Makes a DELETE request to the specified endpoint with query parameters.
     *
     * @param endpoint    The endpoint relative to the base URI.
     * @param queryParams Query parameters to append to the URI.
     * @param headers     Additional headers (can be null).
     */
    public void deleteRequest(String endpoint, Map<String, String> headers, Map<String, String> queryParams) {
        try {
            URI uri = buildUri(endpoint, queryParams);
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);
            restTemplate.exchange(uri, HttpMethod.DELETE, requestEntity, Void.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Error during DELETE request to " + endpoint, e);
        }
    }

    /**
     * Generic method for making HTTP requests.
     *
     * @param method        HTTP method (e.g., POST, PUT, PATCH).
     * @param endpoint      The endpoint relative to the base URI.
     * @param body          Request body (can be null).
     * @param headers       Additional headers (can be null).
     * @param queryParams   Query parameters to append to the URI.
     * @param responseClass Expected response type.
     * @return The response body.
     */
    private <T> T exchangeRequest(HttpMethod method, String endpoint, Object body, Map<String, String> headers, Map<String, String> queryParams, Class<T> responseClass) {
        try {
            URI uri = buildUri(endpoint, queryParams);
            HttpHeaders httpHeaders = createHeaders(headers);

            HttpEntity<Object> requestEntity = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<T> response = restTemplate.exchange(uri, method, requestEntity, responseClass);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Error during " + method.name() + " request to " + endpoint, e);
        }
    }

    /**
     * Builds a complete URI with query parameters.
     *
     * @param endpoint    The endpoint relative to the base URI.
     * @param queryParams Query parameters to append.
     * @return The complete URI.
     */
    private URI buildUri(String endpoint, Map<String, String> queryParams) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(assistantProperties.getBaseUri() + endpoint);

        if (queryParams != null) {
            queryParams.forEach(uriBuilder::queryParam);
        }

        return uriBuilder.build().toUri();
    }

    /**
     * Creates HTTP headers, including Authorization and Content-Type.
     *
     * @param additionalHeaders Additional headers to include (can be null).
     * @return The complete HttpHeaders object.
     */
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
