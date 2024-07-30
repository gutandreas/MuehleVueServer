package edu.andreasgut.MuehleWebSpringVue.Websocket.AllInOneWebsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class GameActionWebsocketHandler {

    @MessageMapping("/game/action")
    public void handleGameActionMessage(
            @Payload String message,
            SimpMessageHeaderAccessor headerAccessor
    ){
        headerAccessor.getSessionId();
    }
}
