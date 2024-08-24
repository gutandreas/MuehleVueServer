package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;


import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // oder JOINED, je nach Modellierung
@DiscriminatorColumn(name = "player_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Player extends Participant {


    private STONECOLOR stonecolor;
    private PHASE currentPhase;
    private int numberOfStonesPut;
    private int numberOfStonesLost;
    private int numberOfStonesKilled;

    public Player(String name, STONECOLOR stonecolor, PHASE phase) {
        super(name);
        this.stonecolor = stonecolor;
        this.currentPhase = phase;
        this.numberOfStonesPut = 0;
        this.numberOfStonesLost = 0;
        this.numberOfStonesKilled = 0;
    }



    public Player() {

    }


    public STONECOLOR getStonecolor() {
        return stonecolor;
    }


    public PHASE getCurrentPhase() {
        return currentPhase;
    }

    public int getNumberOfStonesPut() {
        return numberOfStonesPut;
    }

    public int getNumberOfStonesLost() {
        return numberOfStonesLost;
    }

    public int getNumberOfStonesKilled() {
        return numberOfStonesKilled;
    }

    public void increaseNumberOfStonesPut(){
        numberOfStonesPut++;
    }

    public void increaseNumberOfStonesLost(){
        numberOfStonesLost++;
    }

    public void increaseNumberOfStonesKilled(){
        numberOfStonesKilled++;
    }

    public void setCurrentPhase(PHASE currentPhase) {
        this.currentPhase = currentPhase;
    }

    abstract Move move(Board board, int playerIndex, boolean allowedToJump);

    abstract Position put(Board board, int playerIndex);

    abstract Position kill(Board board, int otherPlayerIndex);




}
