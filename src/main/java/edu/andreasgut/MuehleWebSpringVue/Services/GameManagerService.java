package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.Exceptions.InvalidSetupException;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.*;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Random;

@Service
public class GameManagerService {

    private static final Logger logger = LoggerFactory.getLogger(GameManagerService.class);

    private final GameRepository gameRepository;
    private PairingService pairingService;
    private GameService gameService;
    private ComputerService computerService;
    private MainService mainService;
    private SenderService senderService;
    private StatisticService statisticService;
    private GameStateService gameStateService;


    public GameManagerService(GameRepository gameRepository, PairingService pairingService, GameService gameService, ComputerService computerService, MainService mainService, SenderService senderService, StatisticService statisticService, GameStateService gameStateService) {
        this.gameRepository = gameRepository;
        this.pairingService = pairingService;
        this.gameService = gameService;
        this.computerService = computerService;
        this.mainService = mainService;
        this.senderService = senderService;
        this.statisticService = statisticService;
        this.gameStateService = gameStateService;
    }

    public LinkedList<Game> getAllGames() {
        LinkedList<Game> games = gameRepository.findAll();
        return games;
    }

    public LinkedList<Game> getActiveGames() {
        LinkedList<Game> games = gameRepository.findByGameState_FinishedFalse();
        return games;
    }

    public boolean doesGameExist(String gameCode) {
        return gameRepository.findByGameCode(gameCode) != null;
    }

    public void deleteGameByGameCode(String gameCode) {
        gameRepository.deleteGameByGameCode(gameCode);
    }


    @Transactional
    public Game setupLoginGameStart(JsonObject jsonRequest, String webSocketSessionId) {

        String gameCode = jsonRequest.get("gamecode").getAsString();
        STONECOLOR playerStonecolor = jsonRequest.get("stonecolor").getAsString().equals("b") ? STONECOLOR.BLACK : STONECOLOR.WHITE;
        String firstStone = jsonRequest.get("firststone").getAsString();
        int startPlayerIndex = firstStone.equals("e") ? 1 : 2;
        PHASE phase = startPlayerIndex == 1 ? PHASE.PUT : PHASE.WAIT;

        Player humanPlayerStart = new HumanPlayer(jsonRequest.get("name").getAsString(), playerStonecolor, webSocketSessionId, phase);
        Pairing pairing = new Pairing(humanPlayerStart, startPlayerIndex);

        Game gameStart = new Game(gameCode, new GameState(), pairing, new Board());

        if (gameRepository.existsByGameCode(gameCode)){
            throw new InvalidSetupException("Ein Game mit Gamecode " + gameCode + " existiert bereits. Wähle einen anderen Gamecode!");
        } else {
            gameRepository.save(gameStart);
            senderService.sendStatisticUpdate(statisticService.getUpdatedStatistic());
            logger.info("Neues Logingame (start) erstellt");
            return gameStart;
        }
    }


    public Game setupLoginGameJoin(JsonObject jsonRequest, String webSocketSessionId) {

        System.out.println(getClass().getSimpleName() + "- Logingame (join) beigetreten");
        String gameCode = jsonRequest.get("gamecode").getAsString();
        if (!gameRepository.existsByGameCode(gameCode)) {
            throw new InvalidSetupException("Es existiert kein Game mit dem Gamecode " + gameCode + ". Kontrolliere den Gamecode oder eröffne ein neues Spiel.");
        } else if (gameRepository.findByGameCode(gameCode).getPairing().isComplete()) {
            throw new InvalidSetupException("Das Game mit dem Gamecode " + gameCode + " ist bereits komplett. Ein Beitreten ist darum nicht erlaubt.");
        } else {
            Game game = gameRepository.findByGameCode(gameCode);
            Pairing pairing = game.getPairing();

            STONECOLOR playerStonecolorJoin = game.getPairing().getPlayer1().getStonecolor() == STONECOLOR.BLACK ? STONECOLOR.WHITE : STONECOLOR.BLACK;
            PHASE phase = game.getPairing().getCurrentPlayerIndex() == 2 ? PHASE.PUT : PHASE.WAIT;
            Player humanPlayerJoin = new HumanPlayer(jsonRequest.get("name").getAsString(), playerStonecolorJoin, webSocketSessionId, phase);
            pairingService.addSecondPlayer(pairing, humanPlayerJoin);
            gameRepository.save(game);
            senderService.sendStatisticUpdate(statisticService.getUpdatedStatistic());
            logger.info("Neues Logingame (join) erstellt");

            return game;
        }
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

        Game game = new Game(gameCode, new GameState(), pairing, new Board());
        if (computerPlayer.getCurrentPhase() == PHASE.PUT){
            mainService.letComputerStart(game);
        }
        gameRepository.save(game);
        senderService.sendStatisticUpdate(statisticService.getUpdatedStatistic());

        return game;

    }

    public Game addSpectatorToGame(JsonObject jsonRequest, String webSocketSessionId) {
        logger.info("Spectator wird hinzugefügt...");
        String name = jsonRequest.get("name").getAsString();
        String gameCode = jsonRequest.get("gamecode").getAsString();
        boolean isRoboter = jsonRequest.get("isroboter").getAsBoolean();

        Game game = gameRepository.findByGameCode(gameCode);

        gameService.addSpectator(game, new Spectator(name, isRoboter, webSocketSessionId));
        gameRepository.save(game);

        return game;
    }

    public void giveUpGame(JsonObject jsonRequest, String webSocketSessionId) {
        String gameCode = jsonRequest.get("gamecode").getAsString();
        int index = jsonRequest.get("index").getAsInt();
        Game game = gameRepository.findByGameCode(gameCode);
        gameStateService.finishGame(game.getGameState());
        gameStateService.setWinner(game.getGameState(), index == 1 ? 2 : 1);
        senderService.sendGameUpdate(new GameUpdateDto(game, LocalDateTime.now()));
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
