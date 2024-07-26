package edu.andreasgut.MuehleWebSpringVue.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.util.LinkedList;


import java.util.List;

@Entity
public class Game {

    @Id
    private String gameCode;
    private int round;
    private PHASE phase;
    private boolean finished;

    @Transient
    private Board board;

    @Transient
    private Pairing pairing;

    @Transient
    private List<Spectator> spectators = new LinkedList<>();



    public Game(String gameCode, Board board, Pairing pairing, int round) {
        this.gameCode = gameCode;
        this.board = board;
        this.pairing = pairing;
        this.round = round;
        updatePhase();

        this.finished = false;
    }

    public Game() {
        // Leerer Konstruktor für Spring Repository
    }

    public void updatePhase(){
        if (round <= 17){
            phase = PHASE.SET;
        } else {
            int numberOfStonesPlayer1 = board.getNumberOfStates(POSITIONSTATE.PLAYER1);
            int numberOfStonesPlayer2 = board.getNumberOfStates(POSITIONSTATE.PLAYER2);
            if (numberOfStonesPlayer1 <= 3 && numberOfStonesPlayer2 <= 3){
                phase = PHASE.JUMPALL;
            } else if (numberOfStonesPlayer1 <= 3) {
                phase = PHASE.JUMP1;
            } else if (numberOfStonesPlayer2 <= 3) {
                phase = PHASE.JUMP2;
            } else {
                phase = PHASE.MOVE;
            }
        }
    }

    public void increaseRoundAndUpdatePhase(){
        round++;
        updatePhase();
    }

    public boolean addSpectator(Spectator spectator){
        if (!spectators.contains(spectator)) {
            spectators.add(spectator);
            return true;
        }
        return false;
    }




    // Setter und Getter

    public String getGameCode() {
        return gameCode;
    }

    public Board getBoard() {
        return board;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public PHASE getPhase() {
        return phase;
    }

    public List<Spectator> getSpectators() {
        return spectators;
    }

    public boolean isFinished() {
        return finished;
    }




}

