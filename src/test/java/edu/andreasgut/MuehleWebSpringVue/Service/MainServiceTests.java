package edu.andreasgut.MuehleWebSpringVue.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.BoardRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainServiceTests {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private SenderService senderService;

    @Mock
    private PairingService pairingService;

    @Mock
    private PlayerService playerService;

    @Mock
    private BoardService boardService;



    @InjectMocks
    private MainService mainService;

    @Test
    public void MainService_HandlePut(){

        GameState gameState = new GameState();
        HumanPlayer humanPlayer = new HumanPlayer("Hansli", STONECOLOR.BLACK, "0123123", PHASE.PUT);
        StandardComputerPlayer standardComputerPlayer = new StandardComputerPlayer("Computer", STONECOLOR.WHITE, 3, PHASE.WAIT);
        Pairing pairing = new Pairing(humanPlayer, standardComputerPlayer, 1);  // currentPlayerIndex ist 1
        Board board = new Board();
        Game game = new Game("GD9830", gameState, pairing, board);

        GameUpdateDto gameUpdateDto = new GameUpdateDto(game, LocalDateTime.now());
        when(gameRepository.findByGameCode(any())).thenReturn(game);
        when(pairingService.getPlayerByPlayerUuid(any(), anyString())).thenReturn(humanPlayer);



        // Gson-Instanz erstellen
        Gson gson = new Gson();

        // Konvertiere das Game-Objekt in einen JSON-String
        String jsonString = "{\"type\":\"MOVE\",\"from\":{\"ring\":0,\"field\":1},\"to\":{\"ring\":0,\"field\":2},\"gamecode\":\"0HNN08\",\"uuid\":\"21608300-8d99-4630-bbbb-31243bf4f129\"}";

        // Den JSON-String in ein JsonObject umwandeln
        JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();


        //mainService.handleMove(jsonObject);

        Assertions.assertEquals(POSITIONSTATE.FREE, game.getBoard().getBoardPositionsStates()[0][1]);
    }




}
