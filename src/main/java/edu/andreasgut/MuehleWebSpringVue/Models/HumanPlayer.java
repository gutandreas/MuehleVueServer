package edu.andreasgut.MuehleWebSpringVue.Models;

public class HumanPlayer extends Player{

    public HumanPlayer(String name, STONECOLOR stonecolor) {
        super(name, stonecolor);
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
