package org.jem.lichess.lichessbot.chess.model;

import org.springframework.context.ApplicationEvent;

public class ChessEvent extends ApplicationEvent {

    public ChessEvent(Object source) {
        super(source);
    }

}
