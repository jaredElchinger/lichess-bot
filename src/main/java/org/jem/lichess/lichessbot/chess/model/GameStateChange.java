package org.jem.lichess.lichessbot.chess.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GameStateChange {

    private String gameId;
    private String moves;
    private long wtime;
    private long btime;
    private long winc;
    private long binc;
    private String status;
    private boolean white;
    private boolean isFullGameEvent;

}
