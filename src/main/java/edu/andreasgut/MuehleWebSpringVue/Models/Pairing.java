package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import jakarta.persistence.*;

import java.util.UUID;


@Entity
public class Pairing implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID pairingUuid;

    @OneToOne(cascade = CascadeType.ALL)
    private Player player1;

    @OneToOne(cascade = CascadeType.ALL)
    private Player player2;

    private int currentPlayerIndex;

    private int startPlayerIndex;

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

        this.complete = true;
        this.currentPlayerIndex = startPlayerIndex;
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
        this.currentPlayerIndex = startPlayerIndex;
    }

    public Pairing() {
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getPlayerByIndex(int index){
        return index == 1 ? player1 : player2;
    }

    public Player getCurrentPlayer(){
        return currentPlayerIndex == 1 ? player1 : player2;
    }


    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    @Override
    public Pairing clone() {
        try {
            // Shallow copy mit super.clone()
            Pairing cloned = (Pairing) super.clone();

            // Deep copy der Player-Objekte
            cloned.player1 = (player1 != null) ? player1.clone() : null;
            cloned.player2 = (player2 != null) ? player2.clone() : null;

            return cloned;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
