package edu.andreasgut.MuehleWebSpringVue.Models.GameActions;

import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.GameAction;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;

public class Kill extends GameAction {

    Position killPosition;

    public Kill(Position killPosition) {
        this.killPosition = killPosition;
    }

    public Position getKillPosition() {
        return killPosition;
    }
}
