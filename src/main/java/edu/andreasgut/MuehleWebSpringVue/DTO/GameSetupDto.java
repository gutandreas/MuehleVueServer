package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

public class GameSetupDto {

    private Game game;
    private int index;
    private boolean success;

    public GameSetupDto(Game game, int index, boolean success) {
        this.game = game;
        this.index = index;
        this.success = success;
    }

    public Game getGame() {
        return game;
    }

    public int getIndex() {
        return index;
    }

    public boolean isSuccess() {
        return success;
    }
}
