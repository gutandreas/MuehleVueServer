package edu.andreasgut.MuehleWebSpringVue.Models;


public class Move {
    private Position from, to;


    public Move(Position from, Position to) {
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
