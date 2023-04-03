package org.jem.lichess.lichessbot.service.board.subscribers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jem.lichess.lichessbot.chess.model.ChessEvent;
import org.jem.lichess.lichessbot.chess.model.GameAlert;
import org.jem.lichess.lichessbot.chess.model.GameAlertEvent;
import org.jem.lichess.lichessbot.service.board.model.BoardEvent;
import org.jem.lichess.lichessbot.service.common.model.Game;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class BoardEventStreamSubscriber implements Subscriber<DataBuffer> {

    private final ApplicationEventPublisher publisher;

    @Override
    public void onSubscribe(Subscription subscription) {
        log.info("Starting subscription of Lichess Board Event stream.");
        log.info("Successfully started subscription of Lichess Board Event stream . . . ");
    }

    @Override
    public void onNext(DataBuffer dataBuffer) {

        try (final InputStream inputStream = dataBuffer.asInputStream()) {

            final String line = new String(inputStream.readAllBytes()).trim();
            if (!line.isBlank()) {
                final BoardEvent event = new Gson().fromJson(line, BoardEvent.class);
                final Game game = event.getGame();
                log.info("Received a board event from Lichess for game {}, board event is of type {}.",
                        game == null ? "[null]" : game.getGameId(), event.getType());
                final Optional<? extends ChessEvent> eventOptional = this.fromBoardEvent(event);
                if (eventOptional.isPresent()) {
                    this.publisher.publishEvent(eventOptional.get());
                } else {
                    log.warn("Currently no actions are taken for Board Event {}.", event);
                }
                log.info("Successfully processed board event from Lichess for game {}, board event is of type {}.",
                        game == null ? "[null]" : game.getGameId(), event.getType());
            } else {
                log.debug("Received a keep alive event for Board Event stream.");
            }

        } catch (final Exception e) {
            log.error("Encountered the following exception while receiving Lichess Board Event stream event, ", e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Encountered an error during Lichess Board Event stream, ", throwable);
    }

    @Override
    public void onComplete() {
        log.info("Ending subscription of Lichess Board Event stream.");
        log.warn("Subscription to Lichess Board Event stream has ended . . . ");
    }

    private final Map<BoardEvent.Type, Function<BoardEvent, ? extends ChessEvent>> supply = Map.of(
            BoardEvent.Type.GAME_START, this::fromGameStartOrFinishEventType,
            BoardEvent.Type.GAME_FINISH, this::fromGameStartOrFinishEventType
    );

    private Optional<? extends ChessEvent> fromBoardEvent(final BoardEvent event) {
        return Optional.ofNullable(this.supply.getOrDefault(event.getType(), notFound -> null).apply(event));
    }

    private GameAlertEvent fromGameStartOrFinishEventType(final BoardEvent event) {
        final GameAlert alert = new GameAlert();
        final Game game = event.getGame();

        alert.setGameId(game.getGameId());
        alert.setFen(game.getFen());
        alert.setColor(game.getColor());
        alert.setLastMove(game.getLastMove());
        alert.setSource(game.getSource());
        alert.setSpeed(game.getSpeed());
        alert.setPerf(game.getPerf());
        alert.setRated(game.isRated());
        alert.setHasMoved(game.isHasMoved());
        alert.setMyTurn(game.isMyTurn());

        if (BoardEvent.Type.GAME_START.equals(event.getType())) {
            alert.setGameStart(true);
        } else /*Game Finish*/ {
            alert.setGameStart(false);
        }

        final String username = game.getOpponent().getUsername();
        alert.setOpponentName(username == null ? "Unknown Player" : username);
        alert.setAi(game.getOpponent().getId() == null);

        return new GameAlertEvent(this, alert);
    }

}
