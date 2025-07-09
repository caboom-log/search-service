package site.caboomlog.searchservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${elastic.apiKey}")
    private String apiKey;

    @Bean
    public WebClient webClientBuilder() {
        return WebClient.builder()
                .defaultHeaders(heders -> {
                    heders.set(HttpHeaders.AUTHORIZATION, apiKey);
                    heders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                }).build();
    }
}
