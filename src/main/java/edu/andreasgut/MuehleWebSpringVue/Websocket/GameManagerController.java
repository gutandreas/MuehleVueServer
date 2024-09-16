package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameSetupDto;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.GameManagerService;
import edu.andreasgut.MuehleWebSpringVue.Services.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class GameManagerController {

    private static final Logger logger = LoggerFactory.getLogger(GameManagerController.class);


    private GameManagerService gameManagerService;
    private SenderService senderService;
    private GameRepository gameRepository;


    @Autowired
    public GameManagerController(GameManagerService gameManagerService, SenderService senderService, GameRepository gameRepository) {
        this.gameManagerService = gameManagerService;
        this.senderService = senderService;
        this.gameRepository = gameRepository;
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
                logger.info("GameState mit Gamecode {} existiert, Löschen wird gestartet...", gamecode);
                gameManagerService.deleteGameByGameCode(gamecode);
                senderService.sendGetAllGames();
            } else {
                logger.warn("GameState mit Gamecode {} existiert nicht, Löschen wird abgebrochen", gamecode);

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
    @SendToUser("/queue/reply")
    public GameSetupDto setupComputerGame(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            logger.info("Request für neuen Spielaufbau (Computerspiel) ...");
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Game game = gameManagerService.setupComputerGame(jsonObject, sessionId);
            GameSetupDto gameSetupDto = new GameSetupDto(game, 1, true);
            senderService.sendAddGameToAdmin(gameSetupDto);
            return gameSetupDto;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("GameState konnte nicht erstellt werden");
            return new GameSetupDto(null, 0, false);
        }
    }


    @MessageMapping("/manager/setup/start")
    @SendToUser("/queue/reply")
    public GameSetupDto setupStartGame(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            logger.info("Request für neuen Spielaufbau (Logingame Start) ...");
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Game game = gameManagerService.setupLoginGameStart(jsonObject, sessionId);
            GameSetupDto gameSetupDto = new GameSetupDto(game, 1, true);
            senderService.sendAddGameToAdmin(gameSetupDto);
            return gameSetupDto;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("GameState konnte nicht erstellt werden");
            return new GameSetupDto(null, 0, false);
        }
    }


    @MessageMapping("/manager/setup/join")
    @SendToUser("/queue/reply")
    public GameSetupDto setupJoinGame(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            logger.info("Request für neuen Spielaufbau (Logingame Join) ...");
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Game gameJoin = gameManagerService.setupLoginGameJoin(jsonObject, sessionId);
            GameUpdateDto gameUpdateDto = new GameUpdateDto(gameJoin, LocalDateTime.now());
            senderService.sendUpdateGameToAdmin(gameUpdateDto);
            senderService.sendGameUpdate(gameUpdateDto);
            return new GameSetupDto(gameJoin, 2, true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("GameState konnte nicht erstellt werden");
            return new GameSetupDto(null, 0, false);
        }
    }

    @MessageMapping("/manager/setup/watch")
    @SendToUser("/queue/reply")
    public GameSetupDto watchGame(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            logger.info("Request für eine Spielbeobachtung ...");
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Game game = gameManagerService.addSpectatorToGame(jsonObject, sessionId);
            GameUpdateDto gameUpdateDto = new GameUpdateDto(game, LocalDateTime.now());
            senderService.sendGameUpdate(gameUpdateDto);
            return new GameSetupDto(game, 0, true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Das Spiel kann nicht beobachtet werden");
            return new GameSetupDto(null, 0, false);
        }
    }

}
