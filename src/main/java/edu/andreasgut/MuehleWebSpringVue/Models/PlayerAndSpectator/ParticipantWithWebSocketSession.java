package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import org.springframework.web.socket.WebSocketSession;

public interface ParticipantWithWebSocketSession {

    WebSocketSession getWebSocketSession();
}
