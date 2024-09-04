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
public class StandardComputerPlayer extends Player {

    int level;

    public StandardComputerPlayer(String name, STONECOLOR stonecolor, int level, PHASE phase) {
        super(name, stonecolor, phase);
        this.level = level;
    }

    public StandardComputerPlayer() {

    }

    public Put put(Game game, String uuid) {
        System.out.println("Computerput getriggert");
        LinkedList<Put> possiblePuts = Advisor.getPossiblePuts(game, uuid);
        Random random = new Random();
        return possiblePuts.get(random.nextInt(possiblePuts.size()));
    }

    public Move move(Game game, String uuid) {
        System.out.println("Computermove getriggert");
        return null;
    }

    public Kill kill(Game game, String uuid) {
        System.out.println("Computerkill getriggert");
        LinkedList<Kill> possibleKills = Advisor.getPossibleKills(game, uuid);
        Random random = new Random();
        return possibleKills.get(random.nextInt(possibleKills.size()));
    }

    public Jump jump(Game game, String uuid){
        System.out.println("Computerkill getriggert");
        return null;
    }
}
