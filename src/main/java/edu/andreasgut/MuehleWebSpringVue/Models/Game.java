package edu.andreasgut.MuehleWebSpringVue.Models;


import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.ParticipantGroup;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Game {


    @Id
    private String gameCode;
    private int round;
    private boolean finished;
    @Transient
    private ParticipantGroup participantGroup;




    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_fk")
    private Board board;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pairing_fk")
    private Pairing pairing;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "spectators_fk")
    private List<Spectator> spectators = new LinkedList<>();

    public void setBoard(Board board) {
        this.board = board;
    }

    public ParticipantGroup getGroup() {
        return participantGroup;
    }

    public void setGroup(ParticipantGroup participantGroup) {
        this.participantGroup = participantGroup;
    }


    public Game(String gameCode, Board board, Pairing pairing, int round) {
        this.gameCode = gameCode;
        this.board = board;
        this.pairing = pairing;
        this.round = round;

        this.finished = false;
    }

    public Game() {
        // Leerer Konstruktor für Spring Repository
    }


    public void increaseRound(){
        round++;
    }


    public boolean addSpectator(Spectator spectator){
        if (!spectators.contains(spectator)) {
            spectators.add(spectator);
            return true;
        }
        return false;
    }

    public boolean executePut(Put put, String uuid){
        boolean phaseOk = pairing.getPlayerByPlayerUuid(uuid).getCurrentPhase() == PHASE.SET;
        boolean positionOk = board.getStateOfPosition(put.getPutPosition()) == POSITIONSTATE.FREE;

        if (phaseOk && positionOk){
            int index = getPairing().getPlayerIndexByPlayerUuid(uuid);
            board.putStone(put.getPutPosition(), index);
            Player activePlayer = pairing.getPlayerByIndex(index);
            activePlayer.increaseNumberOfStonesPut();
            Player passivePlayer = pairing.getEnemyOf(activePlayer);
            increaseRound();
            boolean buildsMorris = board.isPositionPartOfMorris(put.getPutPosition());
            if (buildsMorris){
                activePlayer.setCurrentPhase(PHASE.KILL);
                passivePlayer.setCurrentPhase(PHASE.WAIT);
            } else {
                activePlayer.setCurrentPhase(PHASE.WAIT);
                passivePlayer.setCurrentPhase(PHASE.SET);
            }
            return true;
        } else {
            return false;
        }

    }






    // Setter und Getter

    public String getGameCode() {
        return gameCode;
    }

    public Board getBoard() {
        return board;
    }

    public Pairing getPairing() {
        return pairing;
    }


    public List<Spectator> getSpectators() {
        return spectators;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getRound() {
        return round;
    }

    public ParticipantGroup getParticipantGroup() {
        return participantGroup;
    }
}

