package org.jem.lichess.lichessbot.service.common.model;

import com.google.gson.annotations.SerializedName;

public enum Color {

    @SerializedName("white")
    WHITE("white"),
    @SerializedName("black")
    BLACK("black"),
    @SerializedName("random")
    RANDOM("random"),
    ;

    private final String typeValue;

    Color(String s) {
        this.typeValue = s;
    }

}
