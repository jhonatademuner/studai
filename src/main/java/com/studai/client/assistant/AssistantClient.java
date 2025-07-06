package com.studai.client.assistant;

import com.studai.config.properties.AssistantProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.util.List;
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
		HttpHeaders httpHeaders = createHeaders(headers, MediaType.APPLICATION_JSON);
		HttpEntity<Object> requestEntity = new HttpEntity<>(body, httpHeaders);
		return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseClass);
	}

	public <T> ResponseEntity<T> postMultipartRequest(
			String endpoint,
			File file,
			Map<String, String> formFields,
			Map<String, String> headers,
			Class<T> responseClass
	) {
		URI uri = URI.create(assistantProperties.getBaseUri() + endpoint);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new FileSystemResource(file));
		formFields.forEach(body::add);

		HttpHeaders httpHeaders = createHeaders(headers, MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);
		return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseClass);
	}

	private URI buildUri(String endpoint, Map<String, String> params) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(assistantProperties.getBaseUri() + endpoint);

		if (params != null) {
			params.forEach(uriBuilder::queryParam);
		}

		return uriBuilder.build().toUri();
	}

	private HttpHeaders createHeaders(Map<String, String> additionalHeaders, MediaType mediaType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(assistantProperties.getSecurityKey());
		headers.setConnection("keep-alive");
		headers.setContentType(mediaType);

		if (additionalHeaders != null) {
			additionalHeaders.forEach(headers::add);
		}

		return headers;
	}
}
