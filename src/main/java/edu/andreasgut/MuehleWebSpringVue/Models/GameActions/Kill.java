package edu.andreasgut.MuehleWebSpringVue.Models.GameActions;

import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.GameAction;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;

public class Kill extends GameAction {

    Position killPosition;

    public Kill(String playerUuid, Position killPosition) {
        super(playerUuid);
        this.killPosition = killPosition;
    }

    public Position getKillPosition() {
        return killPosition;
    }
}
