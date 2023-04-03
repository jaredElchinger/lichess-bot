package org.jem.lichess.lichessbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class LichessBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(LichessBotApplication.class, args);
    }

}
