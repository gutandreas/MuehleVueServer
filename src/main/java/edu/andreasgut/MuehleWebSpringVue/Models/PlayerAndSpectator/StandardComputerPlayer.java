package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.LinkedList;
import java.util.Random;

@Entity
@DiscriminatorValue("STANDARDCOMPUTER")
public class StandardComputerPlayer extends PlayerPersistent {

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
