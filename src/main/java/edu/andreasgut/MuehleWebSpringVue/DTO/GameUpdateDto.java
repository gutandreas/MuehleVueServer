package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;

import java.time.LocalDateTime;

public class GameUpdateDto {

    private Game game;
    private LocalDateTime timeStamp;

    public GameUpdateDto(Game game, LocalDateTime timeStamp) {
        this.game = game;
        this.timeStamp = timeStamp;
    }

    public Game getGame() {
        return game;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
