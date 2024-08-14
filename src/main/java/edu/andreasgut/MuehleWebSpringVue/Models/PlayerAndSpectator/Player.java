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

    public Player(String name, STONECOLOR stonecolor) {
        super(name);
        this.stonecolor = stonecolor;
        this.currentPhase = PHASE.SET;
    }

    public Player(String name,STONECOLOR stonecolor, PHASE phase) {
        super(name);
        this.stonecolor = stonecolor;
        this.currentPhase = phase;
    }

    public Player() {

    }


    public STONECOLOR getStonecolor() {
        return stonecolor;
    }


    public PHASE getCurrentPhase() {
        return currentPhase;
    }



    abstract Move move(Board board, int playerIndex, boolean allowedToJump);

    abstract Position put(Board board, int playerIndex);

    abstract Position kill(Board board, int otherPlayerIndex);


}
