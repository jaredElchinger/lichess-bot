package org.jem.lichess.lichessbot.service.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Player {

    private String id;
    private String username;
    private int ai;

}
