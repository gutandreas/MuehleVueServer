package edu.andreasgut.MuehleWebSpringVue.Models.GameActions;


import edu.andreasgut.MuehleWebSpringVue.Models.Position;

public class Jump extends GameAction {
    private Position from, to;


    public Jump(Position from, Position to) {
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
        return "Jump auf Position " + from.getRing() + "/" + to.getField();
    }
}
