package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.GameAction;

import java.util.LinkedList;
import java.util.Optional;

public class GameNode {
    private Board board;
    private GameAction gameAction;
    private int currentPlayerIndex;
    private GameNode parent;
    private LinkedList<GameNode> children;
    private int score;
    private GameAction bestAction;

    public GameNode(Board board, GameAction gameAction, int currentPlayerIndex, GameNode parent, int score) {
        this.board = board;
        this.gameAction = gameAction;
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

    public void setScore(int score) {
        this.score = score;
    }

    public GameAction getBestAction() {
        return bestAction;
    }

    public void setBestAction(GameAction bestAction) {
        this.bestAction = bestAction;
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
        System.out.println(prefix + (isTail ? "└── " : "├── ") + "Player: " + currentPlayerIndex + ", Zug: " + gameAction +  ", Score: " + score );

        // Kinderknoten durchlaufen und sie ebenfalls drucken
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).printTree(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.getLast().printTree(prefix + (isTail ? "    " : "│   "), true);
        }
    }
}