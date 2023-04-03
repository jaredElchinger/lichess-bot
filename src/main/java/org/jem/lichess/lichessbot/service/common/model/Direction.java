package org.jem.lichess.lichessbot.service.common.model;

import com.google.gson.annotations.SerializedName;

public enum Direction {

    @SerializedName("in")
    IN("in"),
    @SerializedName("out")
    OUT("out"),
    ;

    private final String typeValue;

    Direction(String s) {
        this.typeValue = s;
    }

}
