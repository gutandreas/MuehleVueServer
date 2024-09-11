package edu.andreasgut.MuehleWebSpringVue.Models;

public class BoardTransient extends Board{

    @Override
    public POSITIONSTATE[][] getBoardPositionsStates() {
        return new POSITIONSTATE[0][];
    }
}
