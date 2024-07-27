package edu.andreasgut.MuehleWebSpringVue.Models.GameActions;

public abstract class GameAction {

    private String playerUuid;

    public GameAction(String playerUuid) {
        this.playerUuid = playerUuid;
    }
}
