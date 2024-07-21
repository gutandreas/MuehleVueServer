package edu.andreasgut.MuehleWebSpringVue.Models;


public class Board {

    POSITIONSTATE[][] boardPositionsStates;

    public Board() {
        boardPositionsStates = new POSITIONSTATE[3][8];
        for (int i = 0; i < boardPositionsStates.length; i++) {
            for (int j = 0; j < boardPositionsStates[0].length; j++) {
                boardPositionsStates[i][j] = POSITIONSTATE.FREE;
            }
        }
    }

    public Board(POSITIONSTATE[][] boardPositionsStates){
        if (boardPositionsStates.length != 3 || boardPositionsStates[0].length != 8){
            throw  new IllegalArgumentException("Arraydimensionen des übergebenen Boards stimmen nicht.");
        } else {
            this.boardPositionsStates = boardPositionsStates;
        }
    }


    public POSITIONSTATE[][] getBoardPositionsStates() {
        return boardPositionsStates;
    }


    public POSITIONSTATE getStateOfPosition(Position position){
        return boardPositionsStates[position.getRing()][position.getField()];
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
