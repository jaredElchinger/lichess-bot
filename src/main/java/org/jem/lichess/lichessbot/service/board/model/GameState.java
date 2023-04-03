package org.jem.lichess.lichessbot.service.board.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GameState {

    // Required
    private String type;
    private String moves;
    private long wtime;
    private long btime;
    private long winc;
    private long binc;
    private GameStatus status;

    //Optional
    private String winner;
    private Boolean wdraw;
    private Boolean bdraw;
    private Boolean wtakeback;
    private Boolean btakeback;

    public enum GameStatus {

        @SerializedName("created")
        CREATED("created"),
        @SerializedName("started")
        STARTED("started"),
        @SerializedName("aborted")
        ABORTED("aborted"),
        @SerializedName("mate")
        MATE("mate"),
        @SerializedName("resign")
        RESIGN("resign"),
        @SerializedName("stalemate")
        STALEMATE("stalemate"),
        @SerializedName("timeout")
        TIMEOUT("timeout"),
        @SerializedName("draw")
        DRAW("draw"),
        @SerializedName("outoftime")
        OUT_OF_TIME("outoftime"),
        @SerializedName("cheat")
        CHEAT("cheat"),
        @SerializedName("noStart")
        NO_START("noStart"),
        @SerializedName("unknownFinish")
        UNKNOWN_FINISH("unknownFinish"),
        @SerializedName("variantEnd")
        VARIANT_END("variantEnd"),
        ;
        private final String typeValue;

        GameStatus(String s) {
            this.typeValue = s;
        }
    }

}
