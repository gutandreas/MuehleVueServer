package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import java.util.LinkedList;

public class GameNode {
    private BoardSimulation board;
    private int currentPlayerIndex;
    private GameNode parent;
    private LinkedList<GameNode> children;

    public GameNode(BoardSimulation board, int currentPlayerIndex, GameNode parent) {
        this.board = board;
        this.currentPlayerIndex = currentPlayerIndex;
        this.parent = parent;
        this.children = new LinkedList<>();
    }

    public BoardSimulation getBoard() {
        return board;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

}