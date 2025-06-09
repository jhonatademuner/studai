package com.studai.client.assistant;

import com.studai.client.assistant.config.AssistantProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class AssistantClient {

	private final RestTemplate restTemplate;
	private final AssistantProperties assistantProperties;

	public AssistantClient(RestTemplate restTemplate, AssistantProperties assistantProperties) {
		this.restTemplate = restTemplate;
		this.assistantProperties = assistantProperties;
	}

	public <T> ResponseEntity<T> postRequest(String endpoint, Object body, Map<String, String> headers, Map<String, String> params, Class<T> responseClass) {
		URI uri = buildUri(endpoint, params);
		HttpHeaders httpHeaders = createHeaders(headers);
		HttpEntity<Object> requestEntity = new HttpEntity<>(body, httpHeaders);
		return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseClass);
	}

	private URI buildUri(String endpoint, Map<String, String> params) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(assistantProperties.getBaseUri() + endpoint);

		if (params != null) {
			params.forEach(uriBuilder::queryParam);
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
