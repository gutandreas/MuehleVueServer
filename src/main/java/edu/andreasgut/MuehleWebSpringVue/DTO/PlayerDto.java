package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

public abstract class PlayerDto {
    private final String name;
    private final STONECOLOR stonecolor;

    public PlayerDto(String name, STONECOLOR stonecolor) {
        this.name = name;
        this.stonecolor = stonecolor;
    }

    public String getName() {
        return name;
    }

    public STONECOLOR getStonecolor() {
        return stonecolor;
    }
}
