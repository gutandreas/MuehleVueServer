package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HUMAN")
public class HumanPlayer extends PlayerPersistent implements ParticipantWithWebSocketSession {

    private String webSocketSessionId;
    public HumanPlayer(String name, STONECOLOR stonecolor, String webSocketSessionId, PHASE phase) {
        super(name, stonecolor, phase);
        this.webSocketSessionId = webSocketSessionId;
    }

    public HumanPlayer() {

    }

    @Override
    public String getWebSocketSessionId() {
        return webSocketSessionId;
    }
}
