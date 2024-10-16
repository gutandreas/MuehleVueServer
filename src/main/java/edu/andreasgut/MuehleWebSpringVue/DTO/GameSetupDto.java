package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;

public class GameSetupDto {

    private Game game;
    private int index;
    private String spectatorName;
    private boolean success;
    private String errorMessage;

    public GameSetupDto(Game game, int index, boolean success) {
        this.game = game;
        this.index = index;
        this.success = success;
        errorMessage = null;
    }

    public GameSetupDto(Game game, int index, boolean success, String errorMessage) {
        this.game = game;
        this.index = index;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public GameSetupDto(Game game, int index, String spectatorName, boolean success) {
        this.game = game;
        this.index = index;
        this.spectatorName = spectatorName;
        this.success = success;
        this.errorMessage = null;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSpectatorName() {
        return spectatorName;
    }
}
