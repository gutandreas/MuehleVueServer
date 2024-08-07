package edu.andreasgut.MuehleWebSpringVue.Websocket;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Services.GameManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@RestController
public class GameManagerController {

    private static final Logger logger = LoggerFactory.getLogger(GameManagerController.class);

    @Autowired
    GameManagerService gameManagerService;

    @GetMapping("/manager/activegames")
    public LinkedList<Game> getActiveGamesSetup(){
        return gameManagerService.getActiveGames();
    }

    @Transactional
    @DeleteMapping("/manager/delete/{gamecode}")
    public ResponseEntity<?> deleteGameByGameCode(@PathVariable(value = "gamecode") String gamecode) {
        logger.info("DELETE request received for gamecode: {}", gamecode);

        boolean gameExists = gameManagerService.doesGameExist(gamecode);

        if (gameExists) {
            logger.info("Game exists, proceeding to delete.");
            gameManagerService.deleteGameByGameCode(gamecode);
            return ResponseEntity.ok().body("Game gelöscht");
        } else {
            logger.warn("Game with gamecode {} does not exist", gamecode);
            return ResponseEntity.badRequest().body("Game mit Gamecode " + gamecode + " existiert nicht");
        }
    }


    @MessageMapping("/manager/activegames")
    public LinkedList<Game> getActiveGames(
            @Payload String message,
            SimpMessageHeaderAccessor headerAccessor
    ){
        System.out.println(headerAccessor.getSessionId());
        System.out.println(message);
        return gameManagerService.getActiveGames();
    }
}
