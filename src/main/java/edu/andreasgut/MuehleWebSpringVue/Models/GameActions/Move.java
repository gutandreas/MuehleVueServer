package edu.andreasgut.MuehleWebSpringVue.Models.GameActions;


import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.GameAction;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;

public class Move extends GameAction {
    private Position from, to;


    public Move(String playerUuid, Position from, Position to) {
        super(playerUuid);
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }


    @Override
    public String toString() {
        return "Zug von Feld " + getFrom().getRing() + "/" + getFrom().getField()
                + " nach " + getTo().getRing() + "/" + getTo().getField();
    }
}
