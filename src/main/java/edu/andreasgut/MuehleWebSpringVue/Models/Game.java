package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import jakarta.persistence.*;

import java.util.LinkedList;


@Entity
public class Game implements Cloneable {

    @Id
    String gameCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "gamestate_fk")
    private GameState gameState;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pairing_fk")
    private Pairing pairing;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "board_fk")
    private Board board;

    @Transient
    private LinkedList<Spectator> spectators = new LinkedList<>();

    public Game(String gameCode, GameState gameState, Pairing pairing, Board board) {
        this.gameCode = gameCode;
        this.gameState = gameState;
        this.pairing = pairing;
        this.board = board;
    }


    public Game() {
    }

    public String getGameCode() {
        return gameCode;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public Board getBoard() {
        return board;
    }

    public LinkedList<Spectator> getSpectators() {
        return spectators;
    }

    @Override
    public Game clone() {
        try {
            Game cloned = (Game) super.clone();

            // Deep copy of the mutable objects
            cloned.gameState = this.gameState != null ? this.gameState.clone() : null;
            cloned.pairing = this.pairing != null ? this.pairing.clone() : null;
            cloned.board = this.board != null ? this.board.clone() : null;
            cloned.spectators = new LinkedList<>();

            return cloned;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Shouldn't happen, since we're Cloneable
        }
    }
}
