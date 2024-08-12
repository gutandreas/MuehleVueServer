package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;


import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public abstract class Player {

    @Id
    private String playerUuid;
    private String name;
    private STONECOLOR stonecolor;
    private PHASE currentPhase;

    public Player(String name,STONECOLOR stonecolor) {
        this.name = name;
        this.playerUuid = UUID.randomUUID().toString();
        this.stonecolor = stonecolor;
        this.currentPhase = PHASE.SET;
    }

    public Player(String name,STONECOLOR stonecolor, PHASE phase) {
        this.name = name;
        this.playerUuid = UUID.randomUUID().toString();
        this.stonecolor = stonecolor;
        this.currentPhase = phase;
    }

    public Player() {

    }


    public String getName() {
        return name;
    }

    public STONECOLOR getStonecolor() {
        return stonecolor;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public PHASE getCurrentPhase() {
        return currentPhase;
    }



    abstract Move move(Board board, int playerIndex, boolean allowedToJump);

    abstract Position put(Board board, int playerIndex);

    abstract Position kill(Board board, int otherPlayerIndex);


}
