package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.*;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Websocket.GameManagerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.Random;

@Service
public class GameManagerService {

    private static final Logger logger = LoggerFactory.getLogger(GameManagerController.class);

    private final GameRepository gameRepository;
    private PairingService pairingService;
    private GameService gameService;




    @Autowired
    public GameManagerService(GameRepository gameRepository, PairingService pairingService, GameService gameService) {
        this.gameRepository = gameRepository;
        this.pairingService = pairingService;
        this.gameService = gameService;
    }

    public LinkedList<GamePersistent> getAllGames() {
        LinkedList<GamePersistent> games = gameRepository.findAll();
        return games;
    }

    public LinkedList<GamePersistent> getActiveGames() {
        LinkedList<GamePersistent> games = gameRepository.findByGameState_FinishedFalse();
        return games;
    }

    public boolean doesGameExist(String gameCode) {
        return gameRepository.findByGameCode(gameCode) != null;
    }

    public void deleteGameByGameCode(String gameCode) {
        gameRepository.deleteGameByGameCode(gameCode);
    }


    @Transactional
    public GamePersistent setupLoginGameStart(JsonObject jsonRequest, String webSocketSessionId) {

        String gameCode = jsonRequest.get("gamecode").getAsString();
        STONECOLOR playerStonecolor = jsonRequest.get("stonecolor").getAsString().equals("b") ? STONECOLOR.BLACK : STONECOLOR.WHITE;
        String firstStone = jsonRequest.get("firststone").getAsString();
        int startPlayerIndex = firstStone.equals("e") ? 1 : 2;
        PHASE phase = startPlayerIndex == 1 ? PHASE.PUT : PHASE.WAIT;

        PlayerPersistent humanPlayerStart = new HumanPlayer(jsonRequest.get("name").getAsString(), playerStonecolor, webSocketSessionId, phase);
        PairingPersistent pairing = new PairingPersistent(humanPlayerStart, startPlayerIndex);

        GamePersistent gameStart = new GamePersistent(gameCode, new GameStatePersistent(), pairing, new BoardPersistent());
        System.out.println(gameStart.getBoard());
        gameRepository.save(gameStart);
        logger.info("Neues Logingame (start) erstellt");
        return gameStart;

    }


    public GamePersistent setupLoginGameJoin(JsonObject jsonRequest, String webSocketSessionId) {

        System.out.println(getClass().getSimpleName() + "- Logingame (join) beigetreten");
        String gameCode = jsonRequest.get("gamecode").getAsString();
        GamePersistent game = gameRepository.findByGameCode(gameCode);
        PairingPersistent pairing = game.getPairing();

        STONECOLOR playerStonecolorJoin = game.getPairing().getPlayer1().getStonecolor() == STONECOLOR.BLACK ? STONECOLOR.WHITE : STONECOLOR.BLACK;
        PHASE phase = game.getPairing().getCurrentPlayerIndex() == 2 ? PHASE.PUT : PHASE.WAIT;
        Player humanPlayerJoin = new HumanPlayer(jsonRequest.get("name").getAsString(), playerStonecolorJoin, webSocketSessionId, phase);
        pairingService.addSecondPlayer(pairing, humanPlayerJoin);
        gameRepository.save(game);
        logger.info("Neues Logingame (join) erstellt");
        return game;

    }

    public GamePersistent setupComputerGame(JsonObject jsonRequest, String webSocketSessionId) {
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
        PairingPersistent pairing = new PairingPersistent(humanPlayer, computerPlayer, startPlayerIndex);
        String gameCode = generateValidGameCode();

        GamePersistent game = new GamePersistent(gameCode, new GameStatePersistent(), pairing, new BoardPersistent());
        gameRepository.save(game);

        return game;

    }

    public GamePersistent addSpectatorToGame(JsonObject jsonRequest, String webSocketSessionId) {
        logger.info("Spectator wird hinzugefügt...");
        String name = jsonRequest.get("name").getAsString();
        String gameCode = jsonRequest.get("gamecode").getAsString();
        boolean isRoboter = jsonRequest.get("isroboter").getAsBoolean();

        GamePersistent game = gameRepository.findByGameCode(gameCode);

        gameService.addSpectator(game, new Spectator(name, isRoboter, webSocketSessionId));
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
