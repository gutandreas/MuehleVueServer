package edu.andreasgut.MuehleWebSpringVue.Models;

public class StandardComputerPlayer extends Player{

    int level;

    public StandardComputerPlayer(String name, STONECOLOR stonecolor, int level) {
        super(name, stonecolor);
        this.level = level;
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
