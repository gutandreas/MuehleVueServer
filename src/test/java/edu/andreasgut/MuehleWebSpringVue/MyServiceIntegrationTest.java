package edu.andreasgut.MuehleWebSpringVue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.MainService;
import edu.andreasgut.MuehleWebSpringVue.Services.SenderService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MyServiceIntegrationTest {

    @Autowired
    private MainService mainService;

    @Autowired
    private GameRepository gameRepository;

    @MockBean
    private SenderService senderService;

    @Test
    public void testServiceWithMockedRepository() throws JSONException {
        // Arrange: Erstelle ein Spiel-Objekt
        GameState gameState = new GameState();
        HumanPlayer humanPlayer = new HumanPlayer("Hansli", STONECOLOR.BLACK, "0123123", PHASE.PUT);
        StandardComputerPlayer standardComputerPlayer = new StandardComputerPlayer("Computer", STONECOLOR.WHITE, 3, PHASE.WAIT);
        Pairing pairing = new Pairing(humanPlayer, standardComputerPlayer, 1);  // currentPlayerIndex ist 1
        Board board = new Board();
        Game game = new Game("GD9830", gameState, pairing, board);


        // JSON-String erstellen und parsen
        String jsonString = "{\"type\":\"PUT\",\"ring\":2,\"field\":6,\"gamecode\":\"334\",\"uuid\":\"3b45b792-4838-4cdb-bbac-980aa289e6bd\"}";
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // Act: handleAction aufrufen, die den Gamecode verwendet
        mainService.handleAction(jsonObject, "1234");

        // Assert: Überprüfen, ob der currentPlayerIndex jetzt 1 ist
        assertEquals(1, game.getPairing().getCurrentPlayerIndex());

    }
}
