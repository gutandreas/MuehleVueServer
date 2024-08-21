package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;

public class GameSetupDto {

    private String gameCode;
    private String uuid;
    private String player1Name;
    private String player2Name;
    private PHASE phase;
    private STONECOLOR stonecolor;
    private int index;

    public GameSetupDto(String gameCode, String uuid, String player1Name, String player2Name, PHASE phase, STONECOLOR stonecolor, int index) {
        this.gameCode = gameCode;
        this.uuid = uuid;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.phase = phase;
        this.stonecolor = stonecolor;
        this.index = index;
    }

    public String getGameCode() {
        return gameCode;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public PHASE getPhase() {
        return phase;
    }

    public STONECOLOR getStonecolor() {
        return stonecolor;
    }

    public int getIndex() {
        return index;
    }
}
