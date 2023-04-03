package org.jem.lichess.lichessbot.service.board.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jem.lichess.lichessbot.service.common.model.Clock;
import org.jem.lichess.lichessbot.service.common.model.GameEventPlayer;
import org.jem.lichess.lichessbot.service.common.model.GameVariant;
import org.jem.lichess.lichessbot.service.common.model.Perf;
import org.jem.lichess.lichessbot.service.common.model.Speed;

@ToString
@Getter
@Setter
public class BoardGameStateEvent {

    private Type type;

    //Game Full Event Fields
    private String id;
    private GameVariant variant;
    private Clock clock;
    private Speed speed;
    private Perf perf;
    private boolean rated;
    private long createdAt;
    private GameEventPlayer white;
    private GameEventPlayer black;
    private GameState state;
    private String tournament;

    //Game State Event Fields
    // Required
    private String moves;
    private long wtime;
    private long btime;
    private long winc;
    private long binc;
    private GameState.GameStatus status;
    //Optional
    private String winner;
    private Boolean wdraw;
    private Boolean bdraw;
    private Boolean wtakeback;
    private Boolean btakeback;

    //Chat Line Event Fields
    private Room room;
    private String username;
    private String text;

    //Opponent Gone Event
    private Boolean gone;
    private Integer claimWinInSeconds;

    @Getter
    public enum Type {

        @SerializedName("gameFull")
        GAME_FULL("gameFull"),

        @SerializedName("gameState")
        GAME_STATE("gameState"),

        @SerializedName("chatLine")
        CHAT_LINE("chatLine"),

        @SerializedName("opponentGone")
        OPPONENT_GONE("opponentGone"),
        ;

        private final String typeValue;

        Type(String s) {
            this.typeValue = s;
        }

    }

    @Getter
    public enum Room {

        @SerializedName("player")
        PLAYER("player"),
        @SerializedName("spectator")
        SPECTATOR("spectator"),
        ;

        private final String typeValue;

        Room(String s) {
            this.typeValue = s;
        }
    }

}
