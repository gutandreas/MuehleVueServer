package edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator;

import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // oder JOINED, je nach Modellierung
@DiscriminatorColumn(name = "player_type", discriminatorType = DiscriminatorType.STRING)
public class PlayerPersistent extends Player{

    public PlayerPersistent(String name, STONECOLOR stonecolor, PHASE phase) {
        super(name, stonecolor, phase);
    }

    public PlayerPersistent() {
    }
}
