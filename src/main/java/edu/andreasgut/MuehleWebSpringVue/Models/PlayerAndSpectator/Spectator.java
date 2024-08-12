package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

public class Spectator implements ParticipantWithWebSocketSession {


    String spectatorId;
    String name;
    boolean isRoboter;
    WebSocketSession webSocketSession;

    public Spectator(String name, boolean isRoboter) {
        this.spectatorId = UUID.randomUUID().toString();
        this.name = name;
        this.isRoboter = isRoboter;
    }

    public String getSpectatorId() {
        return spectatorId;
    }

    public String getName() {
        return name;
    }

    public boolean isRoboter() {
        return isRoboter;
    }

    @Override
    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }
}
