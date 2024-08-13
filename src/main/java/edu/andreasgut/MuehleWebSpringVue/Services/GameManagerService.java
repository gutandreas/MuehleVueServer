package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameDto;
import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.Pairing;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Websocket.GameManagerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Random;

@Service
public class GameManagerService {

    private static final Logger logger = LoggerFactory.getLogger(GameManagerController.class);

    private final GameRepository gameRepository;

    // Konstruktor-Injektion
    public GameManagerService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public LinkedList<Game> getActiveGames() {
        LinkedList<Game> games = gameRepository.findByFinishedFalse();
        return games;
    }

    public boolean doesGameExist(String gameCode) {
        return gameRepository.findByGameCode(gameCode).size() > 0;
    }

    public void deleteGameByGameCode(String gameCode) {
        gameRepository.deleteGameByGameCode(gameCode);
    }

    public void setupComputerGame(JsonObject jsonRequest, String webSocketSessionId) {
        logger.info("Spiel wird aufgebaut");
        STONECOLOR playerStonecolor = jsonRequest.get("color").toString().equals("BLACK") ? STONECOLOR.BLACK : STONECOLOR.WHITE;
        String firstStone = jsonRequest.get("firststone").toString();
        int startPlayerIndex = firstStone.equals("e") ? 1 : 2;

        int level = jsonRequest.get("level").getAsInt();
        String computerName;
        if (level == 0) {
            computerName = "Schwacher Computer";
        } else if (level == 1) {
            computerName = "Mittelstarker Computer";
        } else if (level == 2) {
            computerName = "Starker Computer";
        } else {
            computerName = "Computer (ungültiges Level)";
        }
        STONECOLOR computerStonecolor = playerStonecolor == STONECOLOR.BLACK ? STONECOLOR.WHITE : STONECOLOR.BLACK;

        Player humanPlayer = new HumanPlayer(jsonRequest.get("name").toString(), playerStonecolor, webSocketSessionId);
        Player computerPlayer = new StandardComputerPlayer(computerName, computerStonecolor, level);
        Pairing pairing = new Pairing(humanPlayer, computerPlayer, startPlayerIndex);
        String gameCode = generateValidGameCode();

        Game game = new Game(gameCode, new Board(), pairing, 0);
        gameRepository.save(game);
        System.out.println("Test...");
    }

    private String generateValidGameCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder gameCode = new StringBuilder();
        Random random = new Random();

        do {
            for (int i = 0; i < 6; i++) {
                int index = random.nextInt(characters.length());
                gameCode.append(characters.charAt(index));
            }
        } while (gameRepository.existsByGameCode(gameCode.toString()));

        return gameCode.toString();
    }
}
