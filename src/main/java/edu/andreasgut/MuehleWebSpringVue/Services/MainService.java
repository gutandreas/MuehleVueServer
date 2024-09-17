package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MainService {

    private static final Logger logger = LoggerFactory.getLogger(MainService.class);

    private final GameRepository gameRepository;
    private final SenderService senderService;
    private final BoardService boardService;
    private final PairingService pairingService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final GameStateService gameStateService;
    private final ComputerService computerService;

    @Autowired
    public MainService(GameRepository gameRepository, SenderService senderService, BoardService boardService, PairingService pairingService, PlayerService playerService, GameService gameService, GameStateService gameStateService, ComputerService computerService) {
        this.gameRepository = gameRepository;
        this.senderService = senderService;
        this.boardService = boardService;
        this.pairingService = pairingService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.gameStateService = gameStateService;
        this.computerService = computerService;
    }

    public void handleAction(JsonObject jsonObject, String webSocketSessionId) {
        String type = jsonObject.get("type").getAsString();
        String gameCode = jsonObject.get("gamecode").getAsString();
        logger.info("Action in Game " + gameCode + " wird bearbeitet.");

        switch (type) {
            case "PUT":
                 handlePut(jsonObject);
                 break;
            case "MOVE":
                handleMove(jsonObject);
                break;
            case "KILL":
                handleKill(jsonObject);
                break;
            case "JUMP":
                //handleJump(jsonObject);
                break;
            default:
                logger.warn("Ungültiger Action Type");
        }

        GameUpdateDto updateAfterHumanPlayer = new GameUpdateDto(gameRepository.findByGameCode(gameCode), LocalDateTime.now());
        senderService.sendGameUpdate(updateAfterHumanPlayer);
        Game game = gameRepository.findByGameCode(gameCode);


        while (!gameStateService.isGameFinished(game.getGameState()) && pairingService.getCurrentPlayer(game.getPairing()) instanceof StandardComputerPlayer){
            StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) pairingService.getCurrentPlayer(game.getPairing());
            Player enemyPlayer = pairingService.getEnemyOf(game.getPairing(), standardComputerPlayer);
            int index = pairingService.getIndexOfPlayer(game.getPairing(), standardComputerPlayer);
            PHASE phase = playerService.getPhase(standardComputerPlayer);

            switch (phase){
                case PUT:
                    Put put = computerService.calculatePut(standardComputerPlayer, game.getBoard(), index);
                    boardService.putStone(game.getBoard(), put, index);
                    playerService.increasePutStones(standardComputerPlayer);
                    updateStatesAfterPutOrMove(game.getGameState(), game.getBoard(), game.getPairing(), put);
                    break;
                case MOVE:
                    Move move = computerService.calculateMove(standardComputerPlayer, game.getBoard(), index);
                    boardService.moveStone(game.getBoard(), move, index);
                    updateStatesAfterPutOrMove(game.getGameState(), game.getBoard(), game.getPairing(), move);
                    break;
                case KILL:
                    Kill kill = computerService.calculateKill(standardComputerPlayer, game.getBoard(), index);
                    playerService.increaseKilledStones(standardComputerPlayer);
                    playerService.increaseLostStones(enemyPlayer);
                    boardService.killStone(game.getBoard(), kill);
                    updateStatesAfterPutOrMove(game.getGameState(), game.getBoard(), game.getPairing(), kill);
                    break;
                case JUMP:
                    Jump jump = computerService.calculateJump(standardComputerPlayer, game.getBoard(), index);
                    boardService.jumpStone(game.getBoard(), jump, index);
                    break;

            }

            gameRepository.save(game);
            senderService.sendGameUpdate(new GameUpdateDto(game, LocalDateTime.now()));
        }
    }

    private void handlePut(JsonObject jsonObject) {

        String gameCode = jsonObject.get("gamecode").getAsString();
        Game game = gameRepository.findByGameCode(gameCode);

        try {
            int ring = jsonObject.get("ring").getAsInt();
            int field = jsonObject.get("field").getAsInt();
            String uuid = jsonObject.get("uuid").getAsString();

            Put put = new Put(new Position(ring, field));
            Pairing pairing = game.getPairing();
            Player currentPlayer = pairingService.getCurrentPlayer(pairing);
            Board board = game.getBoard();
            GameState gameState = game.getGameState();

            boolean phaseOK = playerService.isPlayerInPutPhase(currentPlayer);
            boolean uuidOK = pairingService.getPlayerByPlayerUuid(pairing, uuid).equals(pairingService.getCurrentPlayer(pairing));
            boolean positionOK = boardService.isPositionFree(board, put.getPutPosition());

            if (phaseOK && uuidOK && positionOK){
                boardService.putStone(board, put, pairingService.getCurrentPlayerIndex(pairing));
                logger.info("Put ausgeführt in GameState " + gameCode);
                updateStatesAfterPutOrMove(gameState, board, pairing, put);
                playerService.increasePutStones(currentPlayer);
            } else {
                logger.warn("Ungültige Position, Phase oder UUID bei Put in GameState " + gameCode);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Put konnte nicht ausgeführt werden...");
        }

        senderService.sendGameUpdate(new GameUpdateDto(game, LocalDateTime.now()));
        gameRepository.save(game);

    }

    public void handleMove(JsonObject jsonObject) {

        String gameCode = jsonObject.get("gamecode").getAsString();
        Game game = gameRepository.findByGameCode(gameCode);

        try {
            JsonObject fromObject = jsonObject.getAsJsonObject("from");
            int fromRing = fromObject.get("ring").getAsInt();
            int fromField = fromObject.get("field").getAsInt();

            JsonObject toObject = jsonObject.getAsJsonObject("to");
            int toRing = toObject.get("ring").getAsInt();
            int toField = toObject.get("field").getAsInt();

            String uuid = jsonObject.get("uuid").getAsString();
            Move move = new Move(new Position(fromRing, fromField), new Position(toRing, toField));
            Pairing pairing = game.getPairing();
            Player currentPlayer = pairingService.getCurrentPlayer(pairing);
            Board board = game.getBoard();
            GameState gameState = game.getGameState();
            int index = game.getPairing().getCurrentPlayerIndex();

            boolean phaseOK = playerService.isPlayerInPutPhase(currentPlayer);
            boolean uuidOK = pairingService.getPlayerByPlayerUuid(pairing, uuid).equals(pairingService.getCurrentPlayer(pairing));
            boolean toPositionFree = boardService.isPositionFree(board, move.getTo());
            boolean fromPositionWithOwnStone = boardService.isThisPositionOccupiedByPlayerWithIndex(board, index, move.getFrom());
            boolean fromPostionAndToPostionAreNeighbours = boardService.arePositionsNeighbours(move.getFrom(), move.getTo());

            if (phaseOK && uuidOK && toPositionFree && fromPositionWithOwnStone && fromPostionAndToPostionAreNeighbours){
                boardService.moveStone(board, move, pairingService.getCurrentPlayerIndex(pairing));
                logger.info("Put ausgeführt in GameState " + gameCode);
                updateStatesAfterPutOrMove(gameState, board, pairing, move);
            } else {
                logger.warn("Ungültige Position, Phase oder UUID bei Put in GameState " + gameCode);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Put konnte nicht ausgeführt werden...");
        }

        senderService.sendGameUpdate(new GameUpdateDto(game, LocalDateTime.now()));
        gameRepository.save(game);
    }

    public void handleKill(JsonObject jsonObject) {
        String gameCode = jsonObject.get("gamecode").getAsString();
        Game game = gameRepository.findByGameCode(gameCode);

        try {
            int ring = jsonObject.get("ring").getAsInt();
            int field = jsonObject.get("field").getAsInt();
            String uuid = jsonObject.get("uuid").getAsString();

            Kill kill = new Kill(new Position(ring, field));
            Pairing pairing = game.getPairing();
            Player currentPlayer = pairingService.getCurrentPlayer(pairing);
            Board board = game.getBoard();
            GameState gameState = game.getGameState();
            int index = pairingService.getPlayerIndexByPlayerUuid(pairing, uuid);
            int enemyIndex = index == 1 ? 2: 1;

            boolean phaseOK = playerService.isPlayerInKillPhase(currentPlayer);
            boolean uuidOK = pairingService.getPlayerByPlayerUuid(pairing, uuid).equals(pairingService.getCurrentPlayer(pairing));
            boolean positionOK = boardService.isThisPositionOccupiedByPlayerWithIndex(board, enemyIndex, kill.getKillPosition());

            if (phaseOK && uuidOK && positionOK){
                boardService.killStone(board, kill);
                logger.info("Kill ausgeführt in GameState " + gameCode);
                updateStatesAfterPutOrMove(gameState, board, pairing, kill);
                playerService.increaseKilledStones(currentPlayer);
            } else {
                logger.warn("Ungültige Position, Phase oder UUID bei Kill in GameState " + gameCode);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Put konnte nicht ausgeführt werden...");
        }

        senderService.sendGameUpdate(new GameUpdateDto(game, LocalDateTime.now()));
        gameRepository.save(game);


    }

    public GameState handleJump(JsonObject jsonObject, String webSocketSessionId) {
        /*try {
            JsonObject fromObject = jsonObject.getAsJsonObject("from");
            int fromRing = fromObject.get("ring").getAsInt();
            int fromField = fromObject.get("field").getAsInt();

            JsonObject toObject = jsonObject.getAsJsonObject("to");
            int toRing = toObject.get("ring").getAsInt();
            int toField = toObject.get("field").getAsInt();

            String uuid = jsonObject.get("uuid").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();

            Jump jump = new Jump(new Position(fromRing, fromField), new Position(toRing, toField));
            GameState gameState = gameRepository.findByGameCode(gameCode);

            if (gameState.executeJump(jump, uuid)) {
                return gameState;
            } else {
                logger.warn("Ungültige Position bei Jump in GameState " + gameCode);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Jump konnte nicht ausgeführt werden...");
        }*/

        return null;
    }

    private void updateStatesAfterPutOrMove(GameState gameState, Board board, Pairing pairing, GameAction gameAction){

        Position positionToCheck;
        boolean actionBuildsMorris;

        if (gameAction instanceof Put){
            positionToCheck = ((Put) gameAction).getPutPosition();
            actionBuildsMorris = boardService.isPositionPartOfMorris(board, positionToCheck);
        } else if (gameAction instanceof  Kill) {
            actionBuildsMorris = false;
        } else {
            positionToCheck = ((Move) gameAction).getTo();
            actionBuildsMorris = boardService.isPositionPartOfMorris(board, positionToCheck);
        }


        Player currentPlayer = pairingService.getCurrentPlayer(pairing);
        Player enemyPlayer = pairingService.getEnemyOf(pairing, currentPlayer);
        boolean allEnemyStonesInMorris  = boardService.areAllStonesPartOfMorris(board, pairingService.getIndexOfPlayer(pairing, enemyPlayer));
        if (actionBuildsMorris && !allEnemyStonesInMorris){
            playerService.changeToKillPhase(currentPlayer);
            playerService.changeToWaitPhase(enemyPlayer);
        } else {
            playerService.changeToWaitPhase(currentPlayer);
            if (playerService.getNumerOfPutStones(enemyPlayer) < 9){
                playerService.changeToPutPhase(enemyPlayer);
            } else if (playerService.getNumerOfLostStones(enemyPlayer) < 6){
                playerService.changeToMovePhase(enemyPlayer);
            } else {
                playerService.changeToJumpPhase(enemyPlayer);
            }
            pairingService.changeTurn(pairing);
            logger.info("Neuer Aktueller Spieler: " + pairingService.getCurrentPlayer(pairing).getName());
            gameStateService.increaseRound(gameState);
        }
    }

    private void updateStatesAfterMove(GameState gameState, Board board, Pairing pairing, Player currentPlayer, Player enemyPlayer, Move move){
        boolean moveBuildsMorris = boardService.isPositionPartOfMorris(board, move.getTo());
        boolean allEnemyStonesInMorris  = boardService.areAllStonesPartOfMorris(board, pairingService.getIndexOfPlayer(pairing, enemyPlayer));
        if (moveBuildsMorris && !allEnemyStonesInMorris){
            playerService.changeToKillPhase(currentPlayer);
            playerService.changeToWaitPhase(enemyPlayer);
        } else {
            if (playerService.getNumerOfPutStones(currentPlayer) < 9){
                playerService.changeToPutPhase(currentPlayer);
            } else if (playerService.getNumerOfLostStones(currentPlayer) < 6){
                playerService.changeToMovePhase(currentPlayer);
            } else {
                playerService.changeToJumpPhase(currentPlayer);
            }
            pairingService.changeTurn(pairing);
            gameStateService.increaseRound(gameState);
        }
    }

    /*private GameState triggerComputerPlayer(GameState game){
        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) game.getPairing().getCurrentPlayer();
        PHASE phase = standardComputerPlayer.getCurrentPhase();
        String uuid = standardComputerPlayer.getUuid();
        switch (phase){
            case PUT:
                Put put = standardComputerPlayer.put(game, uuid);
                game.executePut(put, uuid);
                break;
            case MOVE:
                Move move = standardComputerPlayer.move(game, uuid);
                game.executeMove(move, uuid);
                break;
            case KILL:
                Kill kill = standardComputerPlayer.kill(game, uuid);
                game.executeKill(kill, uuid);
                break;
            case JUMP:
                Jump jump = standardComputerPlayer.jump(game, uuid);
                game.executeJump(jump, uuid);
                break;
        }
        return game;
    }*/

}



