package edu.andreasgut.MuehleWebSpringVue.Models.GameActions;

import edu.andreasgut.MuehleWebSpringVue.Models.Position;

public class Put extends GameAction{

    Position putPosition;

    public Put(Position putPosition) {
        this.putPosition = putPosition;
    }

    public Position getPutPosition() {
        return putPosition;
    }

    @Override
    public String toString() {
        return "Put auf Position " + putPosition.getRing() + "/" + putPosition.getField();
    }
}
