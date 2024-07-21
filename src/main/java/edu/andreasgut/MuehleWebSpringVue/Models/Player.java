package edu.andreasgut.MuehleWebSpringVue.Models;


import java.util.UUID;

public abstract class Player {

    private final String name;
    private final String playerId;
    private final STONECOLOR stonecolor;

    public Player(String name,STONECOLOR stonecolor) {
        this.name = name;
        this.playerId = UUID.randomUUID().toString();
        this.stonecolor = stonecolor;
    }

    public String getName() {
        return name;
    }

    public STONECOLOR getStonecolor() {
        return stonecolor;
    }

    public String getPlayerId() {
        return playerId;
    }


    abstract Move move(Board board, int playerIndex, boolean allowedToJump);

    abstract Position put(Board board, int playerIndex);

    abstract Position kill(Board board, int otherPlayerIndex);


}
