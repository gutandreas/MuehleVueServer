package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameDto;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Services.GameServices;
import edu.andreasgut.MuehleWebSpringVue.Tools.JSONTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

@Component
public class WebsocketHandler extends TextWebSocketHandler {

    @Autowired
    GameServices gameServices;

    @Autowired
    SetupHandler setupHandler;


    private static final Logger logger = LoggerFactory.getLogger(WebsocketHandler.class);
    List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        webSocketSessions.add(session);
        logger.info("WebSocket connection established: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        webSocketSessions.remove(session);
    }

    @Override
    synchronized public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Extrahiere die Payload der Nachricht
        String payload = message.getPayload();
        System.out.println(payload + " from " + session.getRemoteAddress());

        System.out.println("Neuer Request: Setup Computerspiel");

        // Parst die Payload in ein JsonObject
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
        String category = jsonObject.get("category").getAsString().toLowerCase();

        switch (category) {
            case "setup":
                setupHandler.handleSetupMessages(jsonObject);
                break;
        }
    }

    /*private void sendExceptionMessageToSender(WebSocketSession session, String details) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "exception");
        jsonObject.put("details", details);

        try {
            session.sendMessage(new TextMessage(jsonObject.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
