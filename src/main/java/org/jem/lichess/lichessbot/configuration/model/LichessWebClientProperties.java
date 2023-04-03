package org.jem.lichess.lichessbot.configuration.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("lichess.web")
public class LichessWebClientProperties {

    private String baseUrl;

    private String personalToken;

}
