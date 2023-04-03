package org.jem.lichess.lichessbot.chess.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GameAlert {

    private String gameId;
    private boolean gameStart;
    private String fen;
    private String color;
    private String lastMove;
    private String source;
    private String speed;
    private String perf;
    private boolean rated;
    private boolean hasMoved;
    private boolean isMyTurn;
    private boolean ai;
    private String opponentName;

}
