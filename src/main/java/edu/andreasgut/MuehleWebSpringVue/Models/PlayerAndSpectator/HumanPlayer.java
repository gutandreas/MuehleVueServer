package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.web.socket.WebSocketSession;

@Entity
@DiscriminatorValue("HUMAN")
public class HumanPlayer extends Player implements ParticipantWithWebSocketSession {

    private String webSocketSessionId;
    public HumanPlayer(String name, STONECOLOR stonecolor, String webSocketSessionId) {
        super(name, stonecolor);
        this.webSocketSessionId = webSocketSessionId;
    }

    public HumanPlayer() {

    }

    @Override
    public String getWebSocketSessionId() {
        return webSocketSessionId;
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
