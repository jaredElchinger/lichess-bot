package org.jem.lichess.lichessbot.service.common.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GameVariant {

    private Key key;
    private String name;
    @SerializedName("short")
    private String shrt;

    private enum Key {
        @SerializedName("standard")
        STANDARD("standard"),
        @SerializedName("chess960")
        CHESS960("chess960"),
        @SerializedName("crazyhouse")
        CRAZYHOUSE("crazyhouse"),
        @SerializedName("antichess")
        ANTICHESS("antichess"),
        @SerializedName("atomic")
        ATOMIC("atomic"),
        @SerializedName("horde")
        HORDE("horde"),
        @SerializedName("kingOfTheHill")
        KING_OF_THE_HILL("kingOfTheHill"),
        @SerializedName("racingKings")
        RACING_KINGS("racingKings"),
        @SerializedName("threeCheck")
        THREE_CHECK("threeCheck"),
        @SerializedName("fromPosition")
        FROM_POSITION("fromPosition"),
        ;

        private final String key;

        Key(String key) {
            this.key = key;
        }
    }

}
