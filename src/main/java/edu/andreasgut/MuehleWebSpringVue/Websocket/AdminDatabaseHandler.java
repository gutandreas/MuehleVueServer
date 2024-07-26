package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.GameServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminDatabaseHandler {

    @Autowired
    GameServices gameServices;

    @Autowired
    GameRepository gameRepository;

    public void handleSetupMessages(JsonObject jsonObject){
        try {
            String command = jsonObject.get("command").getAsString();
            switch (command){
                case "get":
                    System.out.println(gameRepository.findAll());
                    break;
                default:
                    System.out.println(this.getClass().getSimpleName() + "- Ungültiger Modus");
            }

        }
        catch (Exception e){
            e.printStackTrace();

        }

    }
}
