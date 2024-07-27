package edu.andreasgut.MuehleWebSpringVue.Websocket.Game;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.GameServices;
import edu.andreasgut.MuehleWebSpringVue.Tools.JSONTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class AdminDatabaseHandler {

    @Autowired
    GameServices gameServices;

    @Autowired
    GameRepository gameRepository;

    public void handleSetupMessages(JsonObject jsonObject, WebSocketSession session){
        try {
            String command = jsonObject.get("command").getAsString();
            switch (command){
                case "get":
                    System.out.println(gameRepository.findAll());
                    String jsonString = JSONTools.getJsonStringOfObject(gameRepository.findAll());
                    TextMessage textMessage = new TextMessage(jsonString);
                    session.sendMessage(textMessage);
                    System.out.println(jsonString);
                    break;
                default:
                    System.out.println(this.getClass().getSimpleName() + "- Ungültiger Command");
            }

        }
        catch (Exception e){
            e.printStackTrace();

        }

    }
}
