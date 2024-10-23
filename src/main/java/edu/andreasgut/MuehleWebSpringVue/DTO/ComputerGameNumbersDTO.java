package edu.andreasgut.MuehleWebSpringVue.DTO;

public class ComputerGameNumbersDTO {

    private String description;
    private int numberOfComputerGames;
    private int numberOfGamesWonByComputer;

    public ComputerGameNumbersDTO(String description, int numberOfComputerGames, int numberOfGamesWonByComputer) {
        this.description = description;
        this.numberOfComputerGames = numberOfComputerGames;
        this.numberOfGamesWonByComputer = numberOfGamesWonByComputer;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfComputerGames() {
        return numberOfComputerGames;
    }

    public int getNumberOfGamesWonByComputer() {
        return numberOfGamesWonByComputer;
    }
}
