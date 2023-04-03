package org.jem.lichess.lichessbot.controller;

import lombok.RequiredArgsConstructor;
import org.jem.lichess.lichessbot.service.board.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/events")
    ResponseEntity<?> subscribeToBoardEvents(@RequestParam boolean listen) {
        if (listen) {
            this.boardService.subscribeToBoardEventStream();
            return new ResponseEntity<>("Board Event Stream subscribed.", HttpStatus.OK);
        } else {
            this.boardService.disposeOfBoardEventStream();
            return new ResponseEntity<>("Board Event Stream disposed.", HttpStatus.OK);
        }
    }

    @GetMapping("/stream")
    ResponseEntity<?> subscribeToBoardGameState(@RequestParam String game, @RequestParam(defaultValue = "true") boolean listen) {
        if (listen) {
            this.boardService.subscribeToStreamOf(game);
            return new ResponseEntity<>("Board Game State Stream subscribed.", HttpStatus.OK);
        } else {
            this.boardService.disposeOfBoardGameEventStream(game);
            return new ResponseEntity<>("Board Game State Stream disposed.", HttpStatus.OK);
        }
    }

}
