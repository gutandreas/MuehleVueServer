package edu.andreasgut.MuehleWebSpringVue.AlphaBeta;

import edu.andreasgut.MuehleWebSpringVue.Models.Pairing;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;


public class PairingSimulation {

    private PlayerSimulation player1;
    private PlayerSimulation player2;
    private int currentPlayerIndex;

    public PairingSimulation(Pairing pairing) {
        this.player1 = new PlayerSimulation(pairing.getPlayer1());
        this.player2 = new PlayerSimulation(pairing.getPlayer2());
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public PlayerSimulation getPlayer1() {
        return player1;
    }

    public PlayerSimulation getPlayer2() {
        return player2;
    }
}
