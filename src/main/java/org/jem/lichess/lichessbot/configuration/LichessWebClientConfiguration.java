package org.jem.lichess.lichessbot.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jem.lichess.lichessbot.configuration.model.LichessWebClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class LichessWebClientConfiguration {

    private final LichessWebClientProperties properties;

    @Bean
    public WebClient lichessWebClient() {
        log.info("Providing Lichess Webclient as {}.", this.properties.getBaseUrl());
        return WebClient.builder()
                .defaultHeader("Authorization", "Bearer " + this.properties.getPersonalToken())
                .uriBuilderFactory(new DefaultUriBuilderFactory(this.properties.getBaseUrl()))
                .build();
    }

}
