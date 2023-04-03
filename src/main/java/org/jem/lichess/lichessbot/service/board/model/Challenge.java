package org.jem.lichess.lichessbot.service.board.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jem.lichess.lichessbot.service.common.model.ChallengeStatus;
import org.jem.lichess.lichessbot.service.common.model.ChallengeUser;
import org.jem.lichess.lichessbot.service.common.model.Color;
import org.jem.lichess.lichessbot.service.common.model.Direction;
import org.jem.lichess.lichessbot.service.common.model.GameVariant;
import org.jem.lichess.lichessbot.service.common.model.Perf;
import org.jem.lichess.lichessbot.service.common.model.Speed;
import org.jem.lichess.lichessbot.service.common.model.TimeControl;

@ToString
@Getter
@Setter
public class Challenge {

    private String id;
    private String url;
    private ChallengeStatus status;
    private ChallengeUser challenger;
    private ChallengeUser destUser;
    private GameVariant variant;
    private boolean rated;
    private Speed speed;
    private TimeControl timeControl;
    private Color color;
    private Perf perf;
    private Direction direction;
    private String initialFen;
    private String declineReason;

}
