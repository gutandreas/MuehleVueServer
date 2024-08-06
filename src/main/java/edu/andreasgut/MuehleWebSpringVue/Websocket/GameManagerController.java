package edu.andreasgut.MuehleWebSpringVue.Websocket;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Services.GameManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@RestController
public class GameManagerController {

    @Autowired
    GameManagerService gameManagerService;

    @GetMapping("/manager/activegames")
    public LinkedList<Game> getActiveGamesSetup(){
        return gameManagerService.getActiveGames();
    }

    @MessageMapping("/manager/activegames")
    public LinkedList<Game> getActiveGames(
            @Payload String message,
            SimpMessageHeaderAccessor headerAccessor
    ){
        System.out.println(headerAccessor.getSessionId());
        System.out.println(message);
        return gameManagerService.getActiveGames();
    }
}
