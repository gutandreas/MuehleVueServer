package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.Pairing;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;

import java.time.LocalDateTime;

public class GameUpdateDto {

    private Game game;

    public GameUpdateDto(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
