package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;


import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

import java.util.UUID;

public abstract class Player {

    private final String name;
    private final String playerUuid;
    private final STONECOLOR stonecolor;

    public Player(String name,STONECOLOR stonecolor) {
        this.name = name;
        this.playerUuid = UUID.randomUUID().toString();
        this.stonecolor = stonecolor;
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


    abstract Move move(Board board, int playerIndex, boolean allowedToJump);

    abstract Position put(Board board, int playerIndex);

    abstract Position kill(Board board, int otherPlayerIndex);


}
