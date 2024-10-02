package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;

import java.util.LinkedList;

public class StatisticsDto {

    private int numberOfActiveGames;
    private int numberOfGamesTotal;
    private int numberOfHumanPlayers;
    private LinkedList<HumanPlayer> activeHumanPlayers;
    private int numberOfSpectators;
    private int numberOfComputerGames;
    private int numberOfGamesWonByComputer;

    public StatisticsDto(int numberOfActiveGames, int numberOfGamesTotal, int numberOfHumanPlayers, LinkedList<HumanPlayer> activeHumanPlayers, int numberOfSpectators, int numberOfComputerGames, int numberOfGamesWonByComputer) {
        this.numberOfActiveGames = numberOfActiveGames;
        this.numberOfGamesTotal = numberOfGamesTotal;
        this.numberOfHumanPlayers = numberOfHumanPlayers;
        this.activeHumanPlayers = activeHumanPlayers;
        this.numberOfSpectators = numberOfSpectators;
        this.numberOfComputerGames = numberOfComputerGames;
        this.numberOfGamesWonByComputer = numberOfGamesWonByComputer;
    }

    public int getNumberOfActiveGames() {
        return numberOfActiveGames;
    }

    public int getNumberOfGamesTotal() {
        return numberOfGamesTotal;
    }

    public int getNumberOfHumanPlayers() {
        return numberOfHumanPlayers;
    }

    public int getNumberOfSpectators() {
        return numberOfSpectators;
    }

    public LinkedList<HumanPlayer> getActiveHumanPlayers() {
        return activeHumanPlayers;
    }

    public int getNumberOfComputerGames() {
        return numberOfComputerGames;
    }

    public int getNumberOfGamesWonByComputer() {
        return numberOfGamesWonByComputer;
    }
}
