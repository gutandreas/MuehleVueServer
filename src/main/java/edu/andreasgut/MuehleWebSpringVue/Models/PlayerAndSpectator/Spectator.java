package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import jakarta.persistence.Entity;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@Entity
public class Spectator extends Participant implements ParticipantWithWebSocketSession {



    boolean isRoboter;
    String webSocketSessionId;

    public Spectator(String name, boolean isRoboter) {
        super(name);
        this.isRoboter = isRoboter;
    }

    public Spectator() {

    }


    public boolean isRoboter() {
        return isRoboter;
    }

    @Override
    public String getWebSocketSessionId() {
        return webSocketSessionId;
    }
}
