package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import edu.andreasgut.MuehleWebSpringVue.Models.GameState;

public class GameTree {

    private GameNode root;
    private GameNode currentNode;

    public GameTree(BoardSimulation initialBoard, int currentPlayerIndex) {
        this.root = new GameNode(initialBoard, currentPlayerIndex, null);
        this.currentNode = root;
    }

    public static int evaluateGame(GameState gameState, int index){
        int ownIndex = index;
        int enemyIndex = ownIndex == 1? 2: 1;
        //int numberOfOwnStones = gameState.getBoard().getNumberOfStonesOfPlayerWithIndex(ownIndex);
        //int numberOfEnemyStones = gameState.getBoard().getNumberOfStonesOfPlayerWithIndex(enemyIndex);
        //int score = numberOfOwnStones - numberOfEnemyStones;

        System.out.println(0);

        return 0;
    }
}
