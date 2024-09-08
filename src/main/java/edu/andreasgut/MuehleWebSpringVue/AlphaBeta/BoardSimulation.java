package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.POSITIONSTATE;

public class BoardSimulation {

    POSITIONSTATE[][] boardPositionsStates;

    public BoardSimulation(Board board) {
        this.boardPositionsStates = board.getBoardPositionsStates();
    }

    public int getNumberOfStonesOfPlayerWithIndex(int index){

        POSITIONSTATE state = index == 1 ? POSITIONSTATE.PLAYER1 : POSITIONSTATE.PLAYER2;
        return getNumberOfStates(state);

    }

    public int getNumberOfStates(POSITIONSTATE positionstate){
        int counter = 0;
        for (int i = 0; i < boardPositionsStates.length; i++) {
            for (int j = 0; j < boardPositionsStates[0].length; j++) {
                if (boardPositionsStates[i][j] == positionstate){
                    counter++;
                }
            }
        }
        return counter;

    }
}
