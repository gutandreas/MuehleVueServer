package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.GamePersistent;
import edu.andreasgut.MuehleWebSpringVue.Models.GameState;

public class GameSetupDto {

    private GamePersistent game;
    private int index;
    private boolean success;

    public GameSetupDto(GamePersistent game, int index, boolean success) {
        this.game = game;
        this.index = index;
        this.success = success;
    }

    public GamePersistent getGame() {
        return game;
    }

    public int getIndex() {
        return index;
    }

    public boolean isSuccess() {
        return success;
    }
}
