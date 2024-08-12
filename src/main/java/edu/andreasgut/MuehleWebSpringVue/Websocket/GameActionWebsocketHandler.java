package edu.andreasgut.MuehleWebSpringVue.Websocket;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class GameActionWebsocketHandler {

    @MessageMapping("/game/action")

    public void handleGameActionMessage(
            @Payload String message,
            SimpMessageHeaderAccessor headerAccessor) {
        System.out.println(headerAccessor.getSessionId());
        System.out.println(message);


        //TODO: Das implementieren aus der Message heraus. Evtl. direkt mit objectMapper eine Instanz des JavaObjekts erstellen.
        /*try {
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

        }*/
    }
}
