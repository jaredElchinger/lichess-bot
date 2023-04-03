package org.jem.lichess.lichessbot.chess.model;

import lombok.Getter;

@Getter
public class GameAlertEvent extends ChessEvent {

    private final GameAlert alert;

    public GameAlertEvent(Object source, final GameAlert alert) {
        super(source);
        this.alert = alert;
    }

}
