package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

public class PlayerSimulation {

    private STONECOLOR stonecolor;
    private PHASE currentPhase;
    private int numberOfStonesPut;
    private int numberOfStonesLost;
    private int numberOfStonesKilled;

    public PlayerSimulation(Player player) {
        this.stonecolor = player.getStonecolor();
        this.currentPhase = player.getCurrentPhase();
        this.numberOfStonesPut = player.getNumberOfStonesPut();
        this.numberOfStonesLost = player.getNumberOfStonesLost();
        this.numberOfStonesKilled = player.getNumberOfStonesKilled();
    }
}
