package org.jem.lichess.lichessbot.service.board.subscribers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.jem.lichess.lichessbot.chess.model.GameStateChange;
import org.jem.lichess.lichessbot.chess.model.GameStateChangeEvent;
import org.jem.lichess.lichessbot.service.board.model.BoardGameStateEvent;
import org.jem.lichess.lichessbot.service.board.model.GameState;
import org.jem.lichess.lichessbot.service.common.model.Speed;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BoardGameStateStreamSubscriber implements Subscriber<DataBuffer> {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final String username;

    public BoardGameStateStreamSubscriber(ApplicationEventPublisher applicationEventPublisher,
                                          @Value("${lichess.web.personal-username}") String username) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.username = username;
    }

    private String gameId;

    public BoardGameStateStreamSubscriber withGameId(final String gameId) {
        this.gameId = gameId;
        return this;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        log.info("Starting subscription of Lichess Board Game State stream for game {}.", this.gameId);
        log.info("Successfully started subscription of Lichess Board Game State stream for game {} . . . ", this.gameId);
    }

    @Override
    public void onNext(DataBuffer dataBuffer) {

        try (final InputStream inputStream = dataBuffer.asInputStream()) {

            final String line = new String(inputStream.readAllBytes()).trim();
            if (!line.isBlank()) {
                log.debug("Received an event for Lichess Board Game State stream for game {}.", this.gameId);
                final BoardGameStateEvent event = new Gson().fromJson(line, BoardGameStateEvent.class);
                final Optional<GameStateChangeEvent> eventOptional = this.fromBoardGameStateEvent(event);
                if (eventOptional.isPresent()) {
                    this.applicationEventPublisher.publishEvent(eventOptional.get());
                } else {
                    log.warn("Currently no actions are taken for Board Game State Event {}.", event);
                }
                log.debug("Successfully acted on Lichess Board Game State stream event for game {}.", this.gameId);
            } else {
                log.debug("Keep alive for event for Board Event stream.");
            }

        } catch (final Exception e) {
            log.error("Encountered the following exception while receiving Lichess Board Game State stream event for game {}, ", this.gameId, e);
            throw new RuntimeException(String.format("Failed to process Lichess Board Game State stream for game %s.", this.gameId), e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Encountered an error during Lichess Board Game State stream for game {}, ", this.gameId, throwable);
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException e = (WebClientResponseException) throwable;
            log.info("Received the following WebClient Exception, with details:\n\t+ Http Status Code: {}\n\t+ Response Body: {}",
                    e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    @Override
    public void onComplete() {
        log.info("Ending subscription of Lichess Board Game State stream for game {}.", this.gameId);
        log.warn("Subscription to Lichess Board Game State stream has ended for game {} . . . ", this.gameId);
    }

    private final Map<BoardGameStateEvent.Type, Function<BoardGameStateEvent, GameStateChangeEvent>> supply = Map.of(
            BoardGameStateEvent.Type.GAME_FULL, this::fromGameFullEventType,
            BoardGameStateEvent.Type.GAME_STATE, this::fromGameStateEventType
    );

    private Optional<GameStateChangeEvent> fromBoardGameStateEvent(final BoardGameStateEvent event) {
        return Optional.ofNullable(this.supply.getOrDefault(event.getType(), notFound -> null).apply(event));
    }

    private GameStateChangeEvent fromGameFullEventType(final BoardGameStateEvent event) {
        final GameStateChange change = new GameStateChange();

        final GameState state = event.getState();
        if (state != null && BoardGameStateEvent.Type.GAME_STATE.getTypeValue().equals(state.getType())) {
            change.setGameId(this.gameId);
            change.setMoves(state.getMoves());
            change.setWinc(state.getWinc());
            change.setBinc(state.getBinc());
            change.setWtime(state.getWtime());
            change.setBtime(state.getBtime());
            change.setStatus(state.getStatus().name());
            final String name = event.getWhite().getId();
            change.setWhite(name != null && name.equalsIgnoreCase(this.username));
            change.setFullGameEvent(true);
            change.setUnlimitedTime(Speed.CORRESPONDENCE.equals(event.getSpeed()));
        } else {
            log.error("Error processing game state for gameFull type, state was: {}", state == null ? "null" : state.toString());
            throw new IllegalArgumentException("Expected state to not be null and of type gameState.");
        }

        return new GameStateChangeEvent(this, change);
    }

    private GameStateChangeEvent fromGameStateEventType(final BoardGameStateEvent event) {
        GameStateChange change = new GameStateChange();
        change.setGameId(this.gameId);
        change.setMoves(event.getMoves());
        change.setWinc(event.getWinc());
        change.setBinc(event.getBinc());
        change.setWtime(event.getWtime());
        change.setBtime(event.getBtime());
        change.setStatus(event.getStatus().name());
        change.setFullGameEvent(false);
        return new GameStateChangeEvent(this, change);
    }

}
