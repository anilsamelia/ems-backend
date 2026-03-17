package com.ems.service;

import com.ems.dto.gemini.GeminiRequest;
import com.ems.dto.gemini.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GeminiChatService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String chat(String prompt) {
        // Build the request payload according to Gemini spec
        GeminiRequest.Part part = new GeminiRequest.Part();
        part.setText(prompt);

        GeminiRequest.Content content = new GeminiRequest.Content();
        content.setParts(List.of(part));

        GeminiRequest geminiRequest = new GeminiRequest();
        geminiRequest.setContents(List.of(content));

        // Make the API call using WebClient
        Mono<GeminiResponse> responseMono = webClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(geminiRequest)
                .retrieve()
                .bodyToMono(GeminiResponse.class);

        // Block to get the result synchronously.
        // This is a transitional step. For a fully reactive pipeline,
        // this method would return Mono<String> and the controller would handle it.
        GeminiResponse response = responseMono.block();

        // Extract and return the text
        if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            return response.getCandidates().get(0).getContent().getParts().get(0).getText();
        }

        return "I'm sorry, I couldn't generate a response.";
    }
}
