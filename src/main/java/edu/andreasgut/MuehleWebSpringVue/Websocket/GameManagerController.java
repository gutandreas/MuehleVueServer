package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Services.GameManagerService;
import edu.andreasgut.MuehleWebSpringVue.Services.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@RestController
public class GameManagerController {

    private static final Logger logger = LoggerFactory.getLogger(GameManagerController.class);


    GameManagerService gameManagerService;
    SenderService senderService;


    @Autowired
    public GameManagerController(GameManagerService gameManagerService, SenderService senderService) {
        this.gameManagerService = gameManagerService;
        this.senderService = senderService;
    }

    @Transactional
    @MessageMapping("/admin/games/delete")
    //@SendTo("/topic/admin/games/delete")
    public void deleteGameByGameCode(@Payload String message) {

        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String gamecode = jsonObject.get("gamecode").getAsString();

            logger.info("DELETE Request für folgenden Gamecode: {}", gamecode);

            boolean gameExists = gameManagerService.doesGameExist(gamecode);

            if (gameExists) {
                logger.info("Game mit Gamecode {} existiert, Löschen wird gestartet...", gamecode);
                gameManagerService.deleteGameByGameCode(gamecode);
                senderService.sendGetAllGames();


            } else {
                logger.warn("Game mit Gamecode {} existiert nicht, Löschen wird abgebrochen", gamecode);

            }
        } catch (Exception e) {
            logger.error("Fehler bei DELETE Request!", e);
        }

    }

    @MessageMapping("/admin/games/getall")
    public void getAllGames() {
        logger.info("Request für alle Games");
        senderService.sendGetAllGames();
    }


    @MessageMapping("/manager/setup/computer")
    //@SendToUser("/queue/reply")
    public ResponseEntity<String> setupComputerGame(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            logger.info("Request für neuen Spielaufbau...");
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Game game = gameManagerService.setupComputerGame(jsonObject, sessionId);
            senderService.sendAddGameToAdmin(game);
            return ResponseEntity.ok().body("Game erstellt");
        } catch (Exception e){
            e.printStackTrace();
            logger.warn("Game konnte nicht erstellt werden");
            return ResponseEntity.badRequest().body("Game konnte nicht erstellt werden");
        }


    }




}
