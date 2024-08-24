package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

public class GameSetupDto {

    private Game game;
    private int index;

    public GameSetupDto(Game game, int index) {
        this.game = game;
        this.index = index;
    }

    public Game getGame() {
        return game;
    }

    public int getIndex() {
        return index;
    }
}
