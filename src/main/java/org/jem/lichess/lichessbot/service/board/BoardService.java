package org.jem.lichess.lichessbot.service.board;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jem.lichess.lichessbot.service.board.subscribers.BoardEventStreamSubscriber;
import org.jem.lichess.lichessbot.service.board.subscribers.BoardGameStateStreamSubscriber;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Disposable;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final WebClient gameClient;

    private final BoardEventStreamSubscriber eventStreamSubscriber;

    private final ObjectFactory<BoardGameStateStreamSubscriber> boardGameStateStreamSubscriberObjectFactory;

    private Disposable boardEventStream;

    private final Map<String, Disposable> boardGameStateStream = new HashMap<>();

    @PostConstruct
    void postConstruct() {
        log.info("Attempting startup of Lichess Board Service . . .");
        this.subscribeToBoardEventStream();
        log.info("Lichess Board service initialized . . . ");
    }

    public void subscribeToBoardEventStream() {
        this.disposeOfBoardEventStream();
        log.info("Board Event service sending event stream subscription request. ");
        this.boardEventStream = this.gameClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stream/event").build())
                .retrieve()
                .bodyToFlux(DataBuffer.class)
                .retry(3)
                .doOnEach(dataBufferSignal -> dataBufferSignal.accept(this.eventStreamSubscriber))
                .subscribe();
        log.info("Board Event service completed request for event stream subscription request. ");
    }

    public void disposeOfBoardEventStream() {
        this.disposeOfStream(this.boardEventStream, "Board Event Stream");
    }

    public void subscribeToStreamOf(final String game) {
        this.disposeOfBoardGameEventStream(game);
        log.info("Board Event service sending game state stream subscription request for game {}. ", game);
        this.boardGameStateStream.put(game, this.gameClient.get()
                .uri(uriBuilder -> uriBuilder.path("/board/game/stream/").path(game).build())
                .retrieve()
                .bodyToFlux(DataBuffer.class)
                .doOnEach(
                        dataBufferSignal -> dataBufferSignal.accept(this.boardGameStateStreamSubscriberObjectFactory.getObject().withGameId(game))
                ).subscribe()
        );
        log.info("Board Event service completed request for board game state stream subscription request for game {}. ", game);
    }

    public void disposeOfBoardGameEventStream(final String game) {
        this.disposeOfStream(this.boardGameStateStream.get(game), String.format("Board Game State Stream of game %s", game));
    }

    public void makeBoardMove(final String game, final String move) {
        this.gameClient.post()
                .uri(uriBuilder -> uriBuilder.path("/board/game/").path(game).path("/move/").path(move).build())
                .retrieve()
                .bodyToMono(String.class)
                .retry(3)
                .subscribe(s -> {
                    log.info("Web client successfully posted move {} for game {}, adn received response of: {}.",
                            move, game, s);
                }, throwable -> {
                    log.error("Encountered an error during Lichess Board Game api request for game {} and move request of {}, ", game, move, throwable);
                    if (throwable instanceof WebClientResponseException) {
                        WebClientResponseException e = (WebClientResponseException) throwable;
                        log.info("Received the following WebClient Exception, with details:\n\t+ Http Status Code: {}\n\t+ Response Body: {}",
                                e.getStatusCode().value(), e.getResponseBodyAsString());
                    }
                });
        log.info("Board Service requested move for game id {} with move of {}", game, move);
    }

    private void disposeOfStream(final Disposable disposable, final String streamName) {
        try {
            if (disposable != null) {
                if (disposable.isDisposed()) {
                    log.info("{} has previously been disposed of.", streamName);
                } else {
                    disposable.dispose();
                    log.warn("Successfully disposed of {}, no longer listening to {}!", streamName, streamName);
                }
            } else {
                log.info("No {} to dispose.", streamName);
            }
        } catch (final Exception e) {
            log.error("An error occurred while attempting to dispose of {}, ", streamName, e);
        }
    }

    @PreDestroy
    void onShutdown() {
        log.info("Attempting shutdown of Board Service . . .");
        this.disposeOfBoardEventStream();
        this.boardGameStateStream.keySet().forEach(this::disposeOfBoardGameEventStream);
        log.info("Board Service destroyed.");
    }

}
