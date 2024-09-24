package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;


import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import jakarta.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // oder JOINED, je nach Modellierung
@DiscriminatorColumn(name = "player_type", discriminatorType = DiscriminatorType.STRING)
public class Player extends Participant implements Cloneable{


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

    public void setNumberOfStonesPut(int numberOfStonesPut) {
        this.numberOfStonesPut = numberOfStonesPut;
    }

    public void setNumberOfStonesLost(int numberOfStonesLost) {
        this.numberOfStonesLost = numberOfStonesLost;
    }

    public void setNumberOfStonesKilled(int numberOfStonesKilled) {
        this.numberOfStonesKilled = numberOfStonesKilled;
    }

    public void setCurrentPhase(PHASE currentPhase) {
        this.currentPhase = currentPhase;
    }

    @Override
    public Player clone() {
        try {
            Player cloned = (Player) super.clone();

            // Deep copy ist für primitive Typen und Enums nicht notwendig, da sie entweder direkt kopiert werden oder immutable sind
            // STONECOLOR und PHASE sind Enums und werden einfach kopiert (Enums sind immutable)

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }


    public PHASE getPhaseIfPutMoveOrJump() {
        if (numberOfStonesPut < 9){
            return PHASE.PUT;
        } else if (numberOfStonesLost < 6) {
            return PHASE.MOVE;
        } else {
            return PHASE.JUMP;
        }
    }
}
