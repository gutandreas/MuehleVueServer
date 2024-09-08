package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;


public class GameSimulation {

    private BoardSimulation board;
    private PairingSimulation pairing;
    private int round;


    public GameSimulation(Game game) {
        this.round = game.getRound();
        this.board = new BoardSimulation(game.getBoard());
        this.pairing = new PairingSimulation(game.getPairing());
    }



    private boolean isGameWon(){
        return round > 18 && (board.getNumberOfStonesOfPlayerWithIndex(1) < 3 || board.getNumberOfStonesOfPlayerWithIndex(2) < 3);
    }

}
