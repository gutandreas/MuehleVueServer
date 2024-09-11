package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.LinkedList;

@MappedSuperclass
public abstract class Game {


    private GameState gameState;
    private Pairing pairing;
    private Board board;

    public Game(GameState gameState, Pairing pairing, Board board) {
        this.gameState = gameState;
        this.pairing = pairing;
        this.board = board;
    }


    public Game() {
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
}
