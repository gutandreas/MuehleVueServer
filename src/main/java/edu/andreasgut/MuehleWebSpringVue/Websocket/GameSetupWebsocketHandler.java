package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Services.GameServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


@Component
public class GameSetupWebsocketHandler {

    @Autowired
    GameServices gameServices;

    public void handleSetupMessages(JsonObject jsonObject, WebSocketSession webSocketSession){
        try {
            String modus = jsonObject.get("modus").getAsString();
            switch (modus){
                case "c":
                    gameServices.setupComputerGame(jsonObject, webSocketSession);
                    break;
                case "l":
                    gameServices.setupLoginGame(jsonObject, webSocketSession);
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
