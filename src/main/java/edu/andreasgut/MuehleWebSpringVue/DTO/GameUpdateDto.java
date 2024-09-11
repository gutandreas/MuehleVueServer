package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.GamePersistent;
import edu.andreasgut.MuehleWebSpringVue.Models.GameState;

import java.time.LocalDateTime;

public class GameUpdateDto {

    private GamePersistent game;
    private LocalDateTime timeStamp;

    public GameUpdateDto(GamePersistent game, LocalDateTime timeStamp) {
        this.game = game;
        this.timeStamp = timeStamp;
    }

    public GamePersistent getGame() {
        return game;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
