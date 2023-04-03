package org.jem.lichess.lichessbot.service.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ChallengeUser {

    private Integer rating;
    private Boolean provisional;
    private Boolean online;
    private String id;
    private String name;
    private String title;
    private Boolean patron;

}
