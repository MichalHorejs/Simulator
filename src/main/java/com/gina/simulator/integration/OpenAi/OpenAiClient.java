package com.gina.simulator.integration.OpenAi;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


/**
 * Class responsible for communication with OpenAI API.
 */
@Component
@Slf4j
public class OpenAiClient {

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String baseUrl;

    @Value("${openai.api.model:gpt-4}")
    private String model;

    @Value("${openai.api.key}")
    private String apiKey;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        log.info("OpenAI WebClient initialized with baseUrl={} and model={}", baseUrl, model);
    }

    /**
     * Sends request for chosen model.
     * @param messages chat history with context
     * @return LLM response
     */
    public Mono<String> askModel(List<Map<String, String>> messages) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", messages
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(errorBody -> {
                                    log.error("OpenAI error {}: {}", response.statusCode(), errorBody);
                                    return new RuntimeException("OpenAI API error: " + errorBody);
                                })
                )
                .bodyToMono(JsonNode.class)
                .doOnNext(json -> log.info("GPT Response: {}", json))
                .map(json -> json.get("choices").get(0).get("message").get("content").asText());
    }
}
