package edu.andreasgut.MuehleWebSpringVue.Websocket.Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Services.GameServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

@RestController
@Component
public class GameActionWebsocketHandler {

    @Autowired
    GameServices gameServices;

    @Autowired
    ObjectMapper objectMapper;




    public void handleActionMessages(JsonObject jsonObject, WebSocketSession webSocketSession){
        try {
            String type = jsonObject.get("type").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();
            String playerUuid = jsonObject.get("playeruuid").getAsString();
            switch (type){
                case "put":
                    Put put = objectMapper.readValue(jsonObject.toString(), Put.class);
                    gameServices.handlePut(gameCode, playerUuid, put, webSocketSession);
                    break;
                case "move":
                    Move move = objectMapper.readValue(jsonObject.toString(), Move.class);
                    gameServices.handleMove(gameCode, playerUuid, move, webSocketSession);
                    break;
                case "kill":
                    Kill kill = objectMapper.readValue(jsonObject.toString(), Kill.class);
                    gameServices.handleKill(gameCode, playerUuid, kill, webSocketSession);
                    break;
                default:
                    System.out.println(this.getClass().getSimpleName() + "- Ungültiger Modus");
            }

        }
        catch (Exception e){
            e.printStackTrace();

        }

    }

    private static void setupComputerGame(JsonObject jsonObject){

    }
}
