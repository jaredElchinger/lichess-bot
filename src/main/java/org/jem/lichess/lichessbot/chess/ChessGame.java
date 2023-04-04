package org.jem.lichess.lichessbot.chess;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jem.lichess.lichessbot.utilities.Random;

@Slf4j
@RequiredArgsConstructor
@Getter
public class ChessGame {

    private final String pathToChessEngineExecutable;

    private final boolean playForWhite;

    @Setter
    private boolean unlimitedTime;

    @Setter
    private long minMoveTimeMs = 5000;

    @Setter
    private long maxMoveTimeMs = 10000;

    private StockFish stockFish;

    @SneakyThrows
    public void createGame() {
        if (this.stockFish == null) {
            this.stockFish = new StockFish(this.pathToChessEngineExecutable);
            if (!this.stockFish.startEngine()) {
                log.error("Failed to startup stockfish engine . . ");
                throw new IllegalStateException("Unable to start stockfish engine.");
            }
        }
    }

    public boolean shouldCalculateForThisTurn(final String moves) {

        final int numberOfMoves = moves.split(" ").length;
        log.debug("debug logs for the Chess Game should I move check: \n\t+ moves: {}\n\t+ player white: {}\n\t+ Number of Moves: {}",
                moves, this.playForWhite, numberOfMoves);
        if (this.playForWhite) {
            return numberOfMoves % 2 == 0 || numberOfMoves == 1;
        } else {
            return numberOfMoves % 2 != 0;
        }
    }

    public String getCurrentBestMoveFrom(final String moves, long wtime, long btime, long winc, long binc) {
        long waitTime = -1;
        if (this.unlimitedTime) {
            waitTime = Random.longBetween(this.minMoveTimeMs, this.maxMoveTimeMs);
        }
        return this.stockFish.getBestMove(waitTime, moves, wtime, btime, winc, binc);
    }

    public void stopStockFish() {
        this.stockFish.stopEngine();
    }

}
