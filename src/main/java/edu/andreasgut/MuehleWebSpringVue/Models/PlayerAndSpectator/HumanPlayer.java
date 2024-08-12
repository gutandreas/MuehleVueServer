package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import org.springframework.web.socket.WebSocketSession;

public class HumanPlayer extends Player implements ParticipantWithWebSocketSession {

    private WebSocketSession webSocketSession;
    public HumanPlayer(String name, STONECOLOR stonecolor, WebSocketSession webSocketSession) {
        super(name, stonecolor);
        this.webSocketSession = webSocketSession;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    @Override
    Move move(Board board, int playerIndex, boolean allowedToJump) {
        return null;
    }

    @Override
    Position put(Board board, int playerIndex) {
        return null;
    }

    @Override
    Position kill(Board board, int otherPlayerIndex) {
        return null;
    }
}
