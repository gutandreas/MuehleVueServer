package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Services.GameManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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



    @Transactional
    @MessageMapping("/manager/delete")
    @SendTo("/topic/manager")
    public LinkedList<Game> deleteGameByGameCode(@Payload String message) {

        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String gamecode = jsonObject.get("gamecode").getAsString();

            logger.info("DELETE Request für folgenden Gamecode: {}", gamecode);

            boolean gameExists = gameManagerService.doesGameExist(gamecode);

            if (gameExists) {
                logger.info("Game mit Gamecode {} existiert, Löschen wird gestartet...", gamecode);
                gameManagerService.deleteGameByGameCode(gamecode);
                return gameManagerService.getActiveGames();

            } else {
                logger.warn("Game mit Gamecode {} existiert nicht, Löschen wird abgebrochen", gamecode);
                return null;
            }
        } catch (Exception e) {
            logger.error("Fehler bei DELETE Request!", e);
            return null;
        }

    }


    @MessageMapping("/manager/activegames")
    @SendTo("/topic/manager")
    public LinkedList<Game> getActiveGames(
            @Payload String message,
            SimpMessageHeaderAccessor headerAccessor
    ){
        System.out.println(headerAccessor.getSessionId());
        System.out.println(message);
        return gameManagerService.getActiveGames();
    }
}
