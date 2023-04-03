package org.jem.lichess.lichessbot.service.common.model;

import com.google.gson.annotations.SerializedName;

public enum Speed {

    @SerializedName("ultraBullet")
    ULTRA_BULLET("ultraBullet"),
    @SerializedName("bullet")
    BULLET("bullet"),
    @SerializedName("blitz")
    BLITZ("blitz"),
    @SerializedName("rapid")
    RAPID("rapid"),
    @SerializedName("classical")
    CLASSICAL("classical"),
    @SerializedName("correspondence")
    CORRESPONDENCE("correspondence"),
    ;

    private final String typeValue;

    Speed(String s) {
        this.typeValue = s;
    }

}
