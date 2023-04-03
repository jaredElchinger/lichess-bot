package org.jem.lichess.lichessbot.service.game;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jem.lichess.lichessbot.configuration.model.LichessWebClientProperties;
import org.jem.lichess.lichessbot.service.game.subscribers.GameStreamSubscriber;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@Service
@RequiredArgsConstructor
public class GamesService {


    private final LichessWebClientProperties webClientProperties;

    private WebClient gameClient;

    @PostConstruct
    void postConstruct() {
        log.info("Attempting startup of Lichess Game Service . . .");

        this.gameClient = WebClient.builder()
                .defaultHeader("Authorization", "Bearer " + this.webClientProperties.getPersonalToken())
                .baseUrl(this.webClientProperties.getBaseUrl())
                .uriBuilderFactory(new DefaultUriBuilderFactory(this.webClientProperties.getBaseUrl()))
                .build();

        this.getMyOngoingGames();
        log.info("Please see above to see players ongoing games . . .");
        log.info("Lichess Game service initialized . . . ");
    }

    public String getMyOngoingGames() {

        ResponseEntity<String> response = this.gameClient.get()
                .uri(uriBuilder -> uriBuilder.path("/account/playing").queryParam("nb", "1").build())
                .retrieve()
                .toEntity(String.class)
                .block();

        log.info("Response: {}", response.getBody());

        return response.getBody();
    }

    @SneakyThrows
    public void subscribeToStreamOf(final String game) {
        this.gameClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stream/game/").path(game).build())
                .retrieve()
                .bodyToFlux(DataBuffer.class)
                .doOnEach(dataBufferSignal -> dataBufferSignal.accept(new GameStreamSubscriber(game)))
                .subscribe();
    }

}
