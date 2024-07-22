package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.POSITIONSTATE;

public class BoardDto {

    POSITIONSTATE[][] boardPositionsStates;

    public BoardDto() {
        boardPositionsStates = new POSITIONSTATE[3][8];
        for (int i = 0; i < boardPositionsStates.length; i++) {
            for (int j = 0; j < boardPositionsStates[0].length; j++) {
                boardPositionsStates[i][j] = POSITIONSTATE.FREE;
            }
        }
    }

    public POSITIONSTATE[][] getBoardPositionsStates() {
        return boardPositionsStates;
    }
}
