package edu.andreasgut.MuehleWebSpringVue.Websocket;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.GameActionService;
import edu.andreasgut.MuehleWebSpringVue.Services.GameManagerService;
import edu.andreasgut.MuehleWebSpringVue.Services.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class GameActionController {

    private static final Logger logger = LoggerFactory.getLogger(GameActionController.class);

    GameManagerService gameManagerService;
    GameActionService gameActionService;
    SenderService senderService;
    GameRepository gameRepository;


    @Autowired
    public GameActionController(GameManagerService gameManagerService, SenderService senderService, GameActionService gameActionService, GameRepository gameRepository){
        this.gameManagerService = gameManagerService;
        this.senderService = senderService;
        this.gameActionService = gameActionService;
        this.gameRepository = gameRepository;
    };

    @MessageMapping("/game/action")
    public ResponseEntity<String> handleAction(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String sessionId = headerAccessor.getSessionId();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            System.out.println(jsonObject);
            String gameCode = jsonObject.get("gamecode").getAsString();

            logger.info("Action Request für folgenden Gamecode: {}", gameCode);
            boolean gameExists = gameManagerService.doesGameExist(gameCode);

            if (gameExists) {
                Game game = gameRepository.findByGameCode(gameCode);
                int round = game.getRound();
                Player player1 = game.getPairing().getPlayer1();
                Player player2 = game.getPairing().getPlayer2();
                Board board = gameActionService.handleAction(jsonObject, sessionId);
                GameUpdateDto gameUpdateDto = new GameUpdateDto(board, player1, player2, round);
                senderService.sendGameUpdate(gameUpdateDto);
                return ResponseEntity.ok().body("Action wurde ausgeführt...");

            } else {
                return ResponseEntity.badRequest().body("Gamecode ist ungültig...");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            logger.warn("Action konnte nicht ausgeführt werden");
            return ResponseEntity.badRequest().body("Action konnte nicht ausgeführt werden");

            }
    }
}
