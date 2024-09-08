package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;

public class GameTree {

    private GameNode root;
    private GameNode currentNode;

    public GameTree(BoardSimulation initialBoard, int currentPlayerIndex) {
        this.root = new GameNode(initialBoard, currentPlayerIndex, null);
        this.currentNode = root;
    }

    public static int evaluateGame(Game game, int index){
        int ownIndex = index;
        int enemyIndex = ownIndex == 1? 2: 1;
        int numberOfOwnStones = game.getBoard().getNumberOfStonesOfPlayerWithIndex(ownIndex);
        int numberOfEnemyStones = game.getBoard().getNumberOfStonesOfPlayerWithIndex(enemyIndex);
        int score = numberOfOwnStones - numberOfEnemyStones;

        System.out.println(score);

        return score;
    }
}
