package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Services.GameServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GameSetupHandler {

    @Autowired
    GameServices gameServices;

    public void handleSetupMessages(JsonObject jsonObject){
        try {
            String modus = jsonObject.get("modus").getAsString();
            switch (modus){
                case "c":
                    gameServices.setupComputerGame(jsonObject);
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
