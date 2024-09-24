package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;

import java.util.LinkedList;
import java.util.Optional;

public class GameNode {
    private Board board;
    private int currentPlayerIndex;
    private GameNode parent;
    private LinkedList<GameNode> children;
    int score;

    public GameNode(Board board, int currentPlayerIndex, GameNode parent, int score) {
        this.board = board;
        this.currentPlayerIndex = currentPlayerIndex;
        this.parent = parent;
        if (parent != null) {
            parent.addChildNode(this);
        }
        this.children = new LinkedList<>();
        this.score = score;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getScore() {
        return score;
    }

    public void addChildNode(GameNode child){
        children.add(child);
    }
}