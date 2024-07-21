package edu.andreasgut.MuehleWebSpringVue.Models;

public class Pairing {

    private final Player player1;
    private Player player2;
    private Player currentPlayer;
    private final int startPlayerIndex;
    private boolean complete = false;

    public Pairing(Player player1, Player player2, int startPlayerIndex) {

        if (startPlayerIndex != 1 && startPlayerIndex != 2){
            throw new IllegalArgumentException("Ungültiger Playerindex");
        }

        if (player1 == null || player2 == null){
            throw new IllegalArgumentException("Ein übergebener Player ist null");
        }

        this.player1 = player1;
        this.player2 = player2;
        this.startPlayerIndex = startPlayerIndex;

        complete = true;
        currentPlayer = this.startPlayerIndex == 1 ? player1 : player2;
    }

    public Pairing(Player player1, int startPlayerIndex) {

        if (startPlayerIndex != 1 && startPlayerIndex != 2){
            throw new IllegalArgumentException("Ungültiger Playerindex");
        }

        if (player1 == null){
            throw new IllegalArgumentException("Ein übergebener Player ist null");
        }

        this.player1 = player1;
        this.startPlayerIndex = startPlayerIndex;
    }

    public void addSecondPlayer(Player player2) {

        if (player2 == null){
            throw new IllegalArgumentException("Ein übergebener Player ist null");
        }

        this.player2 = player2;

        complete = true;
        currentPlayer = this.startPlayerIndex == 1 ? player1 : player2;
    }



    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void changeTurn() {
        if (currentPlayer.equals(player1)){
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    public int getIndexOfPlayer(Player player){
        return player1.equals(player) ? 1 : 2;
    }

    public boolean isComplete() {
        return complete;
    }
}
