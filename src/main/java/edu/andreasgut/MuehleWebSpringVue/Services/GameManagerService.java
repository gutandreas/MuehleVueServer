package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Websocket.GameManagerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Random;

@Service
public class GameManagerService {

    private static final Logger logger = LoggerFactory.getLogger(GameManagerController.class);

    private final GameRepository gameRepository;


    @Autowired
    public GameManagerService(GameRepository gameRepository, SimpMessagingTemplate messagingTemplate) {
        this.gameRepository = gameRepository;

    }

    public LinkedList<Game> getAllGames() {
        LinkedList<Game> games = gameRepository.findAll();
        return games;
    }

    public LinkedList<Game> getActiveGames() {
        LinkedList<Game> games = gameRepository.findByFinishedFalse();
        return games;
    }

    public boolean doesGameExist(String gameCode) {
        return gameRepository.findByGameCode(gameCode) != null;
    }

    public void deleteGameByGameCode(String gameCode) {
        gameRepository.deleteGameByGameCode(gameCode);
    }


    public Game setupLoginGameStart(JsonObject jsonRequest, String webSocketSessionId) {

        String gameCode = jsonRequest.get("gamecode").getAsString();

        System.out.println(getClass().getSimpleName() + "- Neues Logingame (start) erstellt");
        STONECOLOR playerStonecolor = jsonRequest.get("stonecolor").getAsString().equals("b") ? STONECOLOR.BLACK : STONECOLOR.WHITE;
        String firstStone = jsonRequest.get("firststone").getAsString();
        int startPlayerIndex = firstStone.equals("e") ? 1 : 2;
        PHASE phase = startPlayerIndex == 1 ? PHASE.PUT : PHASE.WAIT;

        Player humanPlayerStart = new HumanPlayer(jsonRequest.get("name").getAsString(), playerStonecolor, webSocketSessionId, phase);
        Pairing pairing = new Pairing(humanPlayerStart, startPlayerIndex);
        Game gameStart = new Game(gameCode, new Board(), pairing, 0);
        gameRepository.save(gameStart);
        return gameStart;

    }


    public Game setupLoginGameJoin(JsonObject jsonRequest, String webSocketSessionId) {

        System.out.println(getClass().getSimpleName() + "- Logingame (join) beigetreten");
        String gameCode = jsonRequest.get("gamecode").getAsString();

        Game gameJoin = gameRepository.findByGameCode(gameCode);
        STONECOLOR playerStonecolorJoin = gameJoin.getPairing().getPlayer1().getStonecolor() == STONECOLOR.BLACK ? STONECOLOR.WHITE : STONECOLOR.BLACK;

        PHASE phase = gameJoin.getPairing().getCurrentPlayerIndex() == 2 ? PHASE.PUT : PHASE.WAIT;
        Player humanPlayerJoin = new HumanPlayer(jsonRequest.get("name").getAsString(), playerStonecolorJoin, webSocketSessionId, phase);
        gameJoin.getPairing().addSecondPlayer(humanPlayerJoin);
        gameRepository.save(gameJoin);
        return gameJoin;

    }

    public Game setupComputerGame(JsonObject jsonRequest, String webSocketSessionId) {
        logger.info("Spiel wird aufgebaut...");
        STONECOLOR playerStonecolor = jsonRequest.get("stonecolor").getAsString().equals("b") ? STONECOLOR.BLACK : STONECOLOR.WHITE;
        String firstStone = jsonRequest.get("firststone").getAsString();
        int startPlayerIndex = firstStone.equals("e") ? 1 : 2;
        PHASE phaseHumanPlayer = startPlayerIndex == 1 ? PHASE.PUT : PHASE.WAIT;
        PHASE phaseComputerPlayer = startPlayerIndex == 2 ? PHASE.PUT : PHASE.WAIT;

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



        Player humanPlayer = new HumanPlayer(jsonRequest.get("name").getAsString(), playerStonecolor, webSocketSessionId, phaseHumanPlayer);
        Player computerPlayer = new StandardComputerPlayer(computerName, computerStonecolor, level, phaseComputerPlayer);
        Pairing pairing = new Pairing(humanPlayer, computerPlayer, startPlayerIndex);
        String gameCode = generateValidGameCode();

        Game game = new Game(gameCode, new Board(), pairing, 0);
        gameRepository.save(game);

        return game;

    }

    public Game addSpectatorToGame(JsonObject jsonRequest, String webSocketSessionId) {
        logger.info("Spectator wird hinzugefügt...");
        String name = jsonRequest.get("name").getAsString();
        String gameCode = jsonRequest.get("gamecode").getAsString();
        boolean isRoboter = jsonRequest.get("isroboter").getAsBoolean();

        Game game = gameRepository.findByGameCode(gameCode);
        game.addSpectator(new Spectator(name, isRoboter, webSocketSessionId));
        gameRepository.save(game);
        return game;
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
