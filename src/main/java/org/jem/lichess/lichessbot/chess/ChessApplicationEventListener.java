package org.jem.lichess.lichessbot.chess;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jem.lichess.lichessbot.chess.model.ChessEvent;
import org.jem.lichess.lichessbot.chess.model.GameAlert;
import org.jem.lichess.lichessbot.chess.model.GameAlertEvent;
import org.jem.lichess.lichessbot.chess.model.GameStateChange;
import org.jem.lichess.lichessbot.chess.model.GameStateChangeEvent;
import org.jem.lichess.lichessbot.service.board.BoardService;
import org.springframework.context.ApplicationListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@EnableRetry
@RequiredArgsConstructor
@Service
public class ChessApplicationEventListener implements ApplicationListener<ChessEvent> {

    private final BoardService boardService;

    private final Map<String, ChessGame> games = new ConcurrentHashMap<>();

    private static final Map<Class<? extends ChessEvent>, Consumer<ChessEvent>> eventMap = new ConcurrentHashMap<>();

    {
        eventMap.put(GameStateChangeEvent.class, this::handleGameStateChangeEvent);
        eventMap.put(GameAlertEvent.class, this::handleGameAlertEvent);
    }

    @Override
    public void onApplicationEvent(ChessEvent event) {
        Class<? extends ChessEvent> clazz = event.getClass();
        if (eventMap.containsKey(clazz)) {
            eventMap.get(clazz).accept(event);
        } else {
            log.warn("Currently no processing is setup for Chess Events of type {}.", clazz.getSimpleName());
        }
    }

    private void handleGameStateChangeEvent(final ChessEvent event) {
        final GameStateChange change = ((GameStateChangeEvent) event).getGameStateChange();
        final String gameId = change.getGameId();
        log.info("Processing game state change for Game Id {}.", gameId);
        if (this.games.containsKey(gameId)) {
            this.performMove(this.games.get(gameId), change);
        } else {
            if (change.isFullGameEvent()) {
                final ChessGame newGame = new ChessGame(change.isWhite());
                newGame.createGame();
                this.performMove(newGame, change);
                this.games.put(gameId, newGame);
            } else {
                this.boardService.subscribeToStreamOf(change.getGameId());
            }
        }
    }

    private void handleGameAlertEvent(final ChessEvent event) {
        final GameAlert alert = ((GameAlertEvent) event).getAlert();

        final String gameId = alert.getGameId();
        final String opponentName = alert.getOpponentName();
        try {
            if (alert.isGameStart()) {
                log.info("Received a game start alert for game {}, with opponent {} . . .", gameId, opponentName);
                this.boardService.subscribeToStreamOf(gameId);
                log.info("Successfully joined game {}, with opponent {}. Bot will commence making moves on behalf of L3m0nJeezy. Best of luck to {}.",
                        gameId, opponentName, opponentName);
            } else {
                log.info("Cleaning up finished game {} with {} . . .", gameId, opponentName);
                if (this.games.containsKey(gameId)) {
                    this.games.remove(gameId).stopStockFish();
                } else {
                    log.info("No game to cleanup for id {}, moving on.", gameId);
                }
                log.info("Successfully cleaned up finished game {} with {} . . .", gameId, opponentName);
            }
        } catch (final Exception e) {
            log.error("Experienced an issue while attempting to process game alert for {} with oppoenent {}. ",
                    gameId, opponentName, e);
            e.printStackTrace();
            throw new RuntimeException(String.format("Processing error for game alert, %s", alert), e);
        }
    }

    @Retryable(retryFor = Exception.class, label = "Perform Move Exception Retry")
    private void performMove(final ChessGame game, final GameStateChange change) {
        if (game.shouldCalculateForThisTurn(change.getMoves())) {
            final String bestMove = game.getCurrentBestMoveFrom(change.getMoves(), change.getWtime(), change.getBtime(),
                    change.getWinc(), change.getBinc()).trim();
            log.info("Taking turn for game id {}, with the best move calculated as {}.", change.getGameId(), bestMove);
            this.boardService.makeBoardMove(change.getGameId(), bestMove);
        } else {
            log.info("Other opponents turn for game id {}.", change.getGameId());
        }
    }

    @PreDestroy
    void onShutdown() {
        log.info("Shutting down Chess Application event listener . . .");
        this.games.forEach((id, game) -> game.stopStockFish());
        log.info("Shutdown Chess Application event listener . . .");
    }

}
