package com.studai.client.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studai.config.properties.AssistantProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AssistantClientTest {

	private MockWebServer mockWebServer;
	private AssistantClient assistantClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		AssistantProperties props = new AssistantProperties();
		props.setBaseUri(mockWebServer.url("/api/").toString());

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new NoOpResponseErrorHandler()); // 👈 prevents exceptions on 4xx/5xx

		assistantClient = new AssistantClient(restTemplate, props);
	}

	@AfterEach
	void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void postRequest_shouldReturnExpectedResponseAndSendProperRequest() throws Exception {
		// Given
		String expectedResponse = "{\"message\": \"Hello, StudAI!\"}";
		mockWebServer.enqueue(new MockResponse()
				.setBody(expectedResponse)
				.setHeader("Content-Type", "application/json")
				.setResponseCode(200));

		Map<String, String> body = Map.of("prompt", "Hi!");
		Map<String, String> headers = Map.of("Custom-Header", "Value");

		// When
		ResponseEntity<TestResponse> response = assistantClient.postRequest(
				"quizzes", body, headers, null, TestResponse.class
		);

		// Then
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getMessage()).isEqualTo("Hello, StudAI!");

		// And: verify request details
		RecordedRequest recorded = mockWebServer.takeRequest();
		assertThat(recorded.getMethod()).isEqualTo("POST");
		assertThat(recorded.getPath()).isEqualTo("/api/quizzes");
		assertThat(recorded.getHeader("Custom-Header")).isEqualTo("Value");
		assertThat(recorded.getHeader("Content-Type")).contains("application/json");

		Map<String, String> sentBody = objectMapper.readValue(recorded.getBody().readUtf8(), Map.class);
		assertThat(sentBody).containsEntry("prompt", "Hi!");
	}

	@Test
	void postRequest_shouldHandleNon200Responses() {
		mockWebServer.enqueue(new MockResponse()
				.setResponseCode(500)
				.setBody("{\"message\":\"Something went wrong\"}") // 👈 Simulate real JSON response
				.setHeader("Content-Type", "application/json"));   // 👈 Required for JSON parsing

		ResponseEntity<TestResponse> response = assistantClient.postRequest(
				"chat",
				Map.of("prompt", "Hi!"),
				Map.of("Custom-Header", "Value"),
				Map.of("lang", "en"),
				TestResponse.class
		);

		assertThat(response.getStatusCodeValue()).isEqualTo(500);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getMessage()).isEqualTo("Something went wrong");
	}


	// Dummy response DTO for testing
	public static class TestResponse {
		private String message;
		public String getMessage() { return message; }
		public void setMessage(String message) { this.message = message; }
	}

	// Custom error handler to allow reading 4xx/5xx responses without throwing
	public static class NoOpResponseErrorHandler implements ResponseErrorHandler {
		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			return false;
		}
		@Override
		public void handleError(ClientHttpResponse response) {}
	}
}
