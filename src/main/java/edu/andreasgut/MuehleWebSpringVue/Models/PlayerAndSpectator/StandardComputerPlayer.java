package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import edu.andreasgut.MuehleWebSpringVue.Models.*;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("STANDARDCOMPUTER")
public class StandardComputerPlayer extends Player {

    int level;

    public StandardComputerPlayer(String name, STONECOLOR stonecolor, int level, PHASE phase) {
        super(name, stonecolor, phase);
        this.level = level;
    }

    public StandardComputerPlayer() {

    }

    public int getLevel() {
        return level;
    }
}
