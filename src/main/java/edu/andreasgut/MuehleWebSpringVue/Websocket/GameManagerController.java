package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameSetupDto;
import edu.andreasgut.MuehleWebSpringVue.DTO.PlayerDto;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import edu.andreasgut.MuehleWebSpringVue.Services.GameManagerService;
import edu.andreasgut.MuehleWebSpringVue.Services.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @SendToUser("/queue/reply")
    public GameSetupDto setupComputerGame(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            logger.info("Request für neuen Spielaufbau (Computerspiel) ...");
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Game game = gameManagerService.setupComputerGame(jsonObject, sessionId);
            senderService.sendAddGameToAdmin(game);
            Player ownPlayer = game.getPairing().getPlayer1();
            Player enemyPlayer = game.getPairing().getPlayer2();
            String uuid = ownPlayer.getUuid();
            String player1Name = ownPlayer.getName();
            String player2Name = enemyPlayer.getName();
            PHASE phase = ownPlayer.getCurrentPhase();
            STONECOLOR stonecolor = ownPlayer.getStonecolor();
            return new GameSetupDto(game.getGameCode(), uuid, player1Name, player2Name, phase, stonecolor, 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Game konnte nicht erstellt werden");
            return null;
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
            senderService.sendAddGameToAdmin(game);
            Player player1 = game.getPairing().getPlayer1();
            return new GameSetupDto(game.getGameCode(), player1.getUuid(), player1.getName(), "---", PHASE.SET, player1.getStonecolor(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Game konnte nicht erstellt werden");
            return null;
        }
    }


    @MessageMapping("/manager/setup/join")
    @SendToUser("/queue/reply")
    public GameSetupDto setupJoinGame(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            logger.info("Request für neuen Spielaufbau (Logingame Join) ...");
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Game game = gameManagerService.setupLoginGameJoin(jsonObject, sessionId);
            senderService.sendUpdateGameToAdmin(game);
            PlayerDto secondPlayer = new PlayerDto(game.getPairing().getPlayer2().getName());
            senderService.sendSecondPlayer(secondPlayer, game.getGameCode());
            senderService.sendAddGameToAdmin(game);
            Player player1 = game.getPairing().getPlayer1();
            Player player2 = game.getPairing().getPlayer2();
            return new GameSetupDto(game.getGameCode(), player2.getUuid(), player1.getName(), player2.getName(), PHASE.SET, player2.getStonecolor(), 2);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Game konnte nicht erstellt werden");
            return null;
        }
    }

    @MessageMapping("/manager/setup/watch")
    //@SendToUser("/queue/reply")
    public ResponseEntity<String> watchGame(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            logger.info("Request für eine Spielbeobachtung ...");
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            Game game = gameManagerService.addSpectatorToGame(jsonObject, sessionId);
            //TODO: Admin und Spieler über neuen Beobachter informieren
            return ResponseEntity.ok().body("Das Spiel wird beobachtet");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Das Spiel kann nicht beobachtet werden");
            return ResponseEntity.badRequest().body("Das Spiel kann nicht beobachtet werden");
        }
    }

}
