package edu.andreasgut.MuehleWebSpringVue.Models;


import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.ParticipantGroup;
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

    @Transient
    private Pairing pairing;

    @Transient
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








}

