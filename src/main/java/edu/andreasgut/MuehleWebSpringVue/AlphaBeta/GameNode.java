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

    public void printTree() {
        printTree("", true);
    }

    // Rekursive Methode zum Drucken des Baums
    private void printTree(String prefix, boolean isTail) {
        // Aktuellen Knoten drucken
        System.out.println(prefix + (isTail ? "└── " : "├── ") + "Player: " + currentPlayerIndex + ", Score: " + score);

        // Kinderknoten durchlaufen und sie ebenfalls drucken
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).printTree(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.getLast().printTree(prefix + (isTail ? "    " : "│   "), true);
        }
    }
}