package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

public class PlayerOwnDto extends PlayerDto{

    String playerId;

    public PlayerOwnDto(String name, STONECOLOR stonecolor, String playerId) {
        super(name, stonecolor);
        this.playerId = playerId;

    }

    public String getPlayerId() {
        return playerId;
    }
}
