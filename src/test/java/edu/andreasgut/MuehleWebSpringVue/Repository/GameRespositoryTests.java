package edu.andreasgut.MuehleWebSpringVue.Repository;

import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GameRespositoryTests {

    @Autowired
    GameRepository gameRepository;

    @Test
    public void GameRepository_existsByGameCode() {

        //Arrange
        GameState gameState = new GameState();
        HumanPlayer humanPlayer = new HumanPlayer("Hansli", STONECOLOR.BLACK, "0123123", PHASE.PUT);
        StandardComputerPlayer standardComputerPlayer = new StandardComputerPlayer("Computer", STONECOLOR.WHITE, 3, PHASE.WAIT);
        Pairing pairing = new Pairing(humanPlayer, standardComputerPlayer, 1);  // currentPlayerIndex ist 1
        Board board = new Board();
        Game game = new Game("GD9830", gameState, pairing, board);
        gameRepository.save(game);

        //Act
        boolean exists = gameRepository.existsByGameCode("GD9830");
        boolean doesntExist = gameRepository.existsByGameCode("KO8923");


        //Assert
        Assertions.assertEquals(true, exists);
        Assertions.assertEquals(false, doesntExist);
    }
}
