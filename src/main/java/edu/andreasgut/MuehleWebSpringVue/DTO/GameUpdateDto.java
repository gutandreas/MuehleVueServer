package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;

public class GameUpdateDto {

    private Board board;
    private Player player1;
    private Player player2;
    private int round;

    public GameUpdateDto(Board board, Player player1, Player player2, int round) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.round = round;

    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getRound() {
        return round;
    }
}
