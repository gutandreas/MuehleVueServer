package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import jakarta.persistence.*;

import java.util.LinkedList;


@Entity
public class Game {

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
}
