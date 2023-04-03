package org.jem.lichess.lichessbot.service.board.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jem.lichess.lichessbot.service.common.model.Game;

@ToString
@Getter
@Setter
public class BoardEvent {

    private Type type;

    //Game Start & Finish Events
    private Game game;

    //Challenge, Challenge Cancel, & Challenge Declined Event
    private Challenge challenge;

    @Getter
    public enum Type {

        @SerializedName("gameStart")
        GAME_START("gameStart"),

        @SerializedName("gameFinish")
        GAME_FINISH("gameFinish"),

        @SerializedName("challenge")
        CHALLENGE("challenge"),

        @SerializedName("challengeCanceled")
        CHALLENGE_CANCELED("challengeCanceled"),

        @SerializedName("challengeDeclined")
        CHALLENGE_DECLINED("challengeDeclined"),
        ;

        private final String typeValue;

        Type(String s) {
            this.typeValue = s;
        }
    }

}
