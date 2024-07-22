package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.Player;

public class PairingDto {

    private final PlayerDto player1;
    private PlayerDto player2;
    private PlayerDto currentPlayer;
    private final int startPlayerIndex;
    private boolean complete = false;

    public PairingDto(PlayerDto player1, PlayerDto player2, int startPlayerIndex) {
        if (startPlayerIndex != 1 && startPlayerIndex != 2){
            throw new IllegalArgumentException("Ungültiger Playerindex");
        }

        if (player1 == null || player2 == null){
            throw new IllegalArgumentException("Ein übergebener Player ist null");
        }

        this.player1 = player1;
        this.player2 = player2;
        this.startPlayerIndex = startPlayerIndex;

        this.complete = true;
        this.currentPlayer = this.startPlayerIndex == 1 ? player1 : player2;
    }

    public PlayerDto getPlayer1() {
        return player1;
    }

    public PlayerDto getPlayer2() {
        return player2;
    }

    public PlayerDto getCurrentPlayer() {
        return currentPlayer;
    }

    public int getStartPlayerIndex() {
        return startPlayerIndex;
    }

    public boolean isComplete() {
        return complete;
    }
}
