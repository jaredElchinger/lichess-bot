package org.jem.lichess.lichessbot.chess.model;

import lombok.Getter;

public class GameStateChangeEvent extends ChessEvent {

    @Getter
    private final GameStateChange gameStateChange;

    public GameStateChangeEvent(Object source, GameStateChange gameStateChange) {
        super(source);
        this.gameStateChange = gameStateChange;
    }

}
