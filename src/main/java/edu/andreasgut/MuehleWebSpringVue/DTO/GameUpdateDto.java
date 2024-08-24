package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Pairing;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;

public class GameUpdateDto {

    private Board board;
    private Pairing pairing;
    private int round;

    public GameUpdateDto(Board board, Pairing pairing, int round) {
        this.board = board;
        this.pairing = pairing;
        this.round = round;
    }

    public Board getBoard() {
        return board;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public int getRound() {
        return round;
    }
}
