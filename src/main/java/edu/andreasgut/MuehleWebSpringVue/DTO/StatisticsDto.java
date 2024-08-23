package edu.andreasgut.MuehleWebSpringVue.DTO;

public class StatisticsDto {

    private int numberOfActiveGames;
    private int numberOfGamesTotal;
    private int numberOfHumanPlayers;
    private int numberOfSpectators;

    public StatisticsDto(int numberOfActiveGames, int numberOfGamesTotal, int numberOfHumanPlayers, int numberOfSpectators) {
        this.numberOfActiveGames = numberOfActiveGames;
        this.numberOfGamesTotal = numberOfGamesTotal;
        this.numberOfHumanPlayers = numberOfHumanPlayers;
        this.numberOfSpectators = numberOfSpectators;
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
}
