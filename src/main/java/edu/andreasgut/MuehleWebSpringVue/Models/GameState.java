package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import jakarta.persistence.MappedSuperclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@MappedSuperclass
public abstract class GameState {

    private static final Logger logger = LoggerFactory.getLogger(GameState.class);

    private int round;
    private boolean finished;
    private int winnerIndex;


    public GameState(int round) {
        this.round = round;
        this.finished = false;
    }

    public GameState() {
        super();
    }



    public void increaseRound(){
        round++;
    }


    public boolean isFinished() {
        return finished;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setWinnerIndex(int winnerIndex) {
        this.winnerIndex = winnerIndex;
    }
}

