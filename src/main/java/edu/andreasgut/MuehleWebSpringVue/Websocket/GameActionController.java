package edu.andreasgut.MuehleWebSpringVue.Websocket;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.MainService;
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
    MainService gameActionService;
    SenderService senderService;
    GameRepository gameRepository;


    @Autowired
    public GameActionController(GameManagerService gameManagerService, SenderService senderService, MainService gameActionService, GameRepository gameRepository){
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
                gameActionService.handleAction(jsonObject);
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
