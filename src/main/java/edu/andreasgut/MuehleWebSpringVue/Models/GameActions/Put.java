package edu.andreasgut.MuehleWebSpringVue.Models.GameActions;

import edu.andreasgut.MuehleWebSpringVue.Models.Position;

public class Put extends GameAction{

    Position putPosition;

    public Put(String playerUuid, Position putPosition) {
        super(playerUuid);
        this.putPosition = putPosition;
    }

    public Position getPutPosition() {
        return putPosition;
    }
}
