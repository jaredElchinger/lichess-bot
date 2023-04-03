package org.jem.lichess.lichessbot.controller;

import lombok.RequiredArgsConstructor;
import org.jem.lichess.lichessbot.service.game.GamesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
public class GameController {

    private final GamesService gamesService;

    @GetMapping(value = "/ongoing", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> getOngoingGames() {
        return new ResponseEntity<>(this.gamesService.getMyOngoingGames(), HttpStatus.OK);
    }

    @GetMapping("/stream")
    ResponseEntity<String> getGameStream(@RequestParam String game) {
        gamesService.subscribeToStreamOf(game);

        return new ResponseEntity<>("Stream completed", HttpStatus.OK);
    }

}
