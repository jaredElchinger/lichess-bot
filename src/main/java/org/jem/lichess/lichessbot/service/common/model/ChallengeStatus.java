package org.jem.lichess.lichessbot.service.common.model;

import com.google.gson.annotations.SerializedName;

public enum ChallengeStatus {

    @SerializedName("created")
    CREATED("created"),
    @SerializedName("offline")
    OFFLINE("offline"),
    @SerializedName("canceled")
    CANCELED("canceled"),
    @SerializedName("declined")
    DECLINED("declined"),
    @SerializedName("accepted")
    ACCEPTED("accepted"),
    ;

    private final String typeValue;

    ChallengeStatus(String s) {
        this.typeValue = s;
    }

}
