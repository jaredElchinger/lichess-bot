package org.jem.lichess.lichessbot.service.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Clock {

    private long limit;
    private long increment;

}
