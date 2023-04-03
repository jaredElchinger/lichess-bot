package org.jem.lichess.lichessbot.service.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Game {

    private String fullId;
    private String gameId;
    private String id;
    private String fen;
    private String color;
    private String lastMove;
    private String source;
    private String speed;
    private String perf;
    private boolean rated;
    private boolean hasMoved;
    private boolean isMyTurn;

    private GameVariant variant;
    private Player opponent;
    private Compat compat;

}
