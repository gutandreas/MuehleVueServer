package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

public class ComputerGameSetupDto {

    private String uuid;
    private String name;
    private PHASE phase;
    private STONECOLOR stonecolor;

    public ComputerGameSetupDto(String uuid, String name, PHASE phase, STONECOLOR stonecolor) {
        this.uuid = uuid;
        this.name = name;
        this.phase = phase;
        this.stonecolor = stonecolor;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public PHASE getPhase() {
        return phase;
    }

    public STONECOLOR getStonecolor() {
        return stonecolor;
    }
}
