package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.ParticipantGroup;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import jakarta.persistence.*;
import org.hibernate.id.factory.internal.AutoGenerationTypeStrategy;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
public class GameStatePersistent extends GameState {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private int round;
    private boolean finished;
    private int winnerIndex;




    public GameStatePersistent(int round) {
        super(round);
    }

    public GameStatePersistent() {
        super();
    }



}
