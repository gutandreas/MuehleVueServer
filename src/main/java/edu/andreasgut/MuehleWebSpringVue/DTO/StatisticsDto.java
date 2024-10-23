package edu.andreasgut.MuehleWebSpringVue.DTO;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;

import java.util.LinkedList;

public class StatisticsDto {

    private int numberOfActiveGames;
    private int numberOfGamesTotal;
    private int numberOfHumanPlayers;
    private LinkedList<HumanPlayer> activeHumanPlayers;
    private int numberOfSpectators;
    private LinkedList<ComputerGameNumbersDTO> computerGameNumbers;


    public StatisticsDto(int numberOfActiveGames, int numberOfGamesTotal, int numberOfHumanPlayers, LinkedList<HumanPlayer> activeHumanPlayers, int numberOfSpectators, LinkedList<ComputerGameNumbersDTO> computerGameNumbers) {
        this.numberOfActiveGames = numberOfActiveGames;
        this.numberOfGamesTotal = numberOfGamesTotal;
        this.numberOfHumanPlayers = numberOfHumanPlayers;
        this.activeHumanPlayers = activeHumanPlayers;
        this.numberOfSpectators = numberOfSpectators;
        this.computerGameNumbers = computerGameNumbers;
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

    public LinkedList<HumanPlayer> getActiveHumanPlayers() {
        return activeHumanPlayers;
    }

    public int getNumberOfSpectators() {
        return numberOfSpectators;
    }

    public LinkedList<ComputerGameNumbersDTO> getComputerGameNumbers() {
        return computerGameNumbers;
    }
}
