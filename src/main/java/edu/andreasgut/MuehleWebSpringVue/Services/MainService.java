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

    public void handleAction(JsonObject jsonObject) {
        String type = jsonObject.get("type").getAsString();
        String gameCode = jsonObject.get("gamecode").getAsString();
        logger.info("Action in Game " + gameCode + " wird bearbeitet.");

        Game game = gameRepository.findByGameCode(gameCode);
        String uuid = jsonObject.get("uuid").getAsString();

        executeActionIfAllowed(jsonObject, type, game, uuid);

        if (isGameOver(game)){
            gameStateService.finishGame(game.getGameState());
            gameStateService.setWinner(game.getGameState(), pairingService.getPlayerIndexByPlayerUuid(game.getPairing(), uuid));
            String winnerName = pairingService.getPlayerByIndex(game.getPairing(),gameStateService.getWinnerIndex(game.getGameState())).getName();
            logger.info("Spiel " + gameCode + " wurde gewonnen von " +  winnerName);
        }

        senderService.sendGameUpdate(new GameUpdateDto(game, LocalDateTime.now()));
        gameRepository.save(game);


        while (!gameStateService.isGameFinished(game.getGameState()) && pairingService.getCurrentPlayer(game.getPairing()) instanceof StandardComputerPlayer){
            triggerComputerPlayer(gameCode, game);

            gameRepository.save(game);
            senderService.sendGameUpdate(new GameUpdateDto(game, LocalDateTime.now()));
        }
    }

    private void triggerComputerPlayer(String gameCode, Game game) {
        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) pairingService.getCurrentPlayer(game.getPairing());
        int index = pairingService.getIndexOfPlayer(game.getPairing(), standardComputerPlayer);
        PHASE phase = playerService.getPhase(standardComputerPlayer);

        switch (phase){
            case PUT:
                Put put = computerService.calculatePut(game, index);
                boardService.putStoneAndSave(game.getBoard(), put, index);
                updateStatesAfterGameAction(game.getGameState(), game.getBoard(), game.getPairing(), put);
                break;
            case MOVE:
                Move move = computerService.calculateMove(game, index);
                boardService.moveStoneAndSave(game.getBoard(), move, index);
                updateStatesAfterGameAction(game.getGameState(), game.getBoard(), game.getPairing(), move);
                break;
            case KILL:
                Kill kill = computerService.calculateKill(game, index);
                boardService.killStoneAndSave(game.getBoard(), kill);
                updateStatesAfterGameAction(game.getGameState(), game.getBoard(), game.getPairing(), kill);
                break;
            case JUMP:
                Jump jump = computerService.calculateJump(game, index);
                boardService.jumpStoneAndSave(game.getBoard(), jump, index);
                updateStatesAfterGameAction(game.getGameState(), game.getBoard(), game.getPairing(), jump);
                break;
        }

        if (isGameOver(game)){
            gameStateService.finishGame(game.getGameState());
            gameStateService.setWinner(game.getGameState(), pairingService.getIndexOfPlayer(game.getPairing(), standardComputerPlayer));
            String winnerName = pairingService.getPlayerByIndex(game.getPairing(),gameStateService.getWinnerIndex(game.getGameState())).getName();
            logger.info("Spiel " + gameCode + " wurde gewonnen von " +  winnerName);
        }
    }

    private void executeActionIfAllowed(JsonObject jsonObject, String type, Game game, String uuid) {
        switch (type) {
            case "PUT":
                 int putRing = jsonObject.get("ring").getAsInt();
                 int putField = jsonObject.get("field").getAsInt();
                 Put put = new Put(new Position(putRing, putField));
                 if (isPutAllowed(game, put, uuid)){
                     handlePut(game, put);
                 }
                 break;
            case "MOVE":
                JsonObject fromObject = jsonObject.getAsJsonObject("from");
                int fromRing = fromObject.get("ring").getAsInt();
                int fromField = fromObject.get("field").getAsInt();
                JsonObject toObject = jsonObject.getAsJsonObject("to");
                int toRing = toObject.get("ring").getAsInt();
                int toField = toObject.get("field").getAsInt();
                Move move = new Move(new Position(fromRing, fromField), new Position(toRing, toField));

                if (isMoveAllowed(game, move, uuid)){
                    handleMove(game, move);
                }
                break;
            case "KILL":
                int killRing = jsonObject.get("ring").getAsInt();
                int killField = jsonObject.get("field").getAsInt();
                Kill kill = new Kill(new Position(killRing, killField));
                if (isKillAllowed(game, kill, uuid)){
                    handleKill(game, kill);
                }
                break;
            case "JUMP":
                JsonObject jumpFromObject = jsonObject.getAsJsonObject("from");
                int jumpFromRing = jumpFromObject.get("ring").getAsInt();
                int jumpFromField = jumpFromObject.get("field").getAsInt();
                JsonObject jumpToObject = jsonObject.getAsJsonObject("to");
                int jumptToRing = jumpToObject.get("ring").getAsInt();
                int jumptToField = jumpToObject.get("field").getAsInt();
                Jump jump = new Jump(new Position(jumpFromRing, jumpFromField), new Position(jumptToRing, jumptToField));

                if (isJumpAllowed(game, jump, uuid)){
                    handleJump(game, jump);
                }
                break;
            default:
                logger.warn("Ungültiger Action Type");
        }
    }

    private boolean isGameOver(Game game){
        int numberOfEnemysStones = boardService.getNumberOfStonesOfPlayerWithIndex(game.getBoard(), game.getPairing().getCurrentPlayerIndex());
        int round = gameStateService.getRound(game.getGameState());
        boolean lostBeacauseOfNumberOfStones = round > 18 && numberOfEnemysStones < 3;
        boolean lostBeacauseOfLockedIn = playerService.isPlayerInMovePhase(game.getPairing().getCurrentPlayer()) && boardService.hasPlayerNoPossibilitiesToMove(game.getBoard(), game.getPairing().getCurrentPlayerIndex());
        return lostBeacauseOfNumberOfStones || lostBeacauseOfLockedIn;
    }


    public boolean isPutAllowed(Game game, Put put, String uuid){

        Pairing pairing = game.getPairing();
        Player currentPlayer = pairingService.getCurrentPlayer(pairing);
        Board board = game.getBoard();

        boolean phaseOK = playerService.isPlayerInPutPhase(currentPlayer);
        boolean uuidOK = pairingService.getPlayerByPlayerUuid(pairing, uuid).equals(pairingService.getCurrentPlayer(pairing));
        boolean positionOK = boardService.isPositionFree(board, put.getPutPosition());

        return phaseOK && uuidOK && positionOK;
    }

    public boolean isMoveAllowed(Game game, Move move, String uuid){
        Pairing pairing = game.getPairing();
        Player currentPlayer = pairingService.getCurrentPlayer(pairing);
        int playerIndex = pairingService.getIndexOfPlayer(pairing, currentPlayer);
        Board board = game.getBoard();


        boolean phaseOK = playerService.isPlayerInMovePhase(currentPlayer);
        boolean uuidOK = pairingService.getPlayerByPlayerUuid(pairing, uuid).equals(pairingService.getCurrentPlayer(pairing));
        boolean toPositionFree = boardService.isPositionFree(board, move.getTo());
        boolean fromPositionWithOwnStone = boardService.isThisPositionOccupiedByPlayerWithIndex(board, playerIndex, move.getFrom());
        boolean fromPostionAndToPostionAreNeighbours = boardService.arePositionsNeighbours(move.getFrom(), move.getTo());

        return phaseOK && uuidOK && toPositionFree && fromPositionWithOwnStone && fromPostionAndToPostionAreNeighbours;
    }

    public boolean isKillAllowed(Game game, Kill kill, String uuid){
        Pairing pairing = game.getPairing();
        Player currentPlayer = pairingService.getCurrentPlayer(pairing);
        int playerIndex = pairingService.getIndexOfPlayer(pairing, currentPlayer);
        int enemyIndex = playerIndex == 1 ? 2 : 1;
        Board board = game.getBoard();

        boolean phaseOK = playerService.isPlayerInKillPhase(currentPlayer);
        boolean uuidOK = pairingService.getPlayerByPlayerUuid(pairing, uuid).equals(pairingService.getCurrentPlayer(pairing));
        boolean positionOK = boardService.isThisPositionOccupiedByPlayerWithIndex(board, enemyIndex, kill.getKillPosition());

        return phaseOK && uuidOK && positionOK;
    }

    public boolean isJumpAllowed(Game game, Jump jump, String uuid){
        Pairing pairing = game.getPairing();
        Player currentPlayer = pairingService.getCurrentPlayer(pairing);
        int playerIndex = pairingService.getIndexOfPlayer(pairing, currentPlayer);
        Board board = game.getBoard();

        boolean phaseOK = playerService.isPlayerInJumpPhase(currentPlayer);
        boolean uuidOK = pairingService.getPlayerByPlayerUuid(pairing, uuid).equals(pairingService.getCurrentPlayer(pairing));
        boolean toPositionFree = boardService.isPositionFree(board, jump.getTo());
        boolean fromPositionWithOwnStone = boardService.isThisPositionOccupiedByPlayerWithIndex(board, playerIndex, jump.getFrom());

        return phaseOK && uuidOK && toPositionFree && fromPositionWithOwnStone;
    }

    public void handlePut(Game game, Put put) {

        boardService.putStoneAndSave(game.getBoard(), put, pairingService.getCurrentPlayerIndex(game.getPairing()));
        logger.info("Put ausgeführt in Game " + game.getGameCode());
        updateStatesAfterGameAction(game.getGameState(), game.getBoard(), game.getPairing(), put);

    }

    public void handleMove(Game game, Move move) {

        boardService.moveStone(game.getBoard(), move, pairingService.getCurrentPlayerIndex(game.getPairing()));
        logger.info("Move ausgeführt in Game " + game.getGameCode());
        updateStatesAfterGameAction(game.getGameState(), game.getBoard(), game.getPairing(), move);


    }

    public void handleKill(Game game, Kill kill) {

        boardService.killStone(game.getBoard(), kill);
        logger.info("Kill ausgeführt in Game " + game.getGameCode());
        updateStatesAfterGameAction(game.getGameState(), game.getBoard(), game.getPairing(), kill);
    }

    public void handleJump(Game game, Jump jump) {

        boardService.jumpStone(game.getBoard(), jump, pairingService.getCurrentPlayerIndex(game.getPairing()));
        logger.info("Jump ausgeführt in Game " + game.getGameCode());
        updateStatesAfterGameAction(game.getGameState(), game.getBoard(), game.getPairing(), jump);
    }

    private void updateStatesAfterGameAction(GameState gameState, Board board, Pairing pairing, GameAction gameAction){

        Position positionToCheck;
        boolean actionBuildsMorris = false;
        Player currentPlayer = pairingService.getCurrentPlayer(pairing);
        Player enemyPlayer = pairingService.getEnemyOf(pairing, currentPlayer);

        if (gameAction instanceof Put){
            playerService.increasePutStones(currentPlayer);
            positionToCheck = ((Put) gameAction).getPutPosition();
            actionBuildsMorris = boardService.isPositionPartOfMorris(board, positionToCheck);
        } else if (gameAction instanceof  Move) {
            positionToCheck = ((Move) gameAction).getTo();
            actionBuildsMorris = boardService.isPositionPartOfMorris(board, positionToCheck);
        } else if (gameAction instanceof  Jump) {
            positionToCheck = ((Jump) gameAction).getTo();
            actionBuildsMorris = boardService.isPositionPartOfMorris(board, positionToCheck);
        } else if (gameAction instanceof  Kill){
            playerService.increaseKilledStones(currentPlayer);
            playerService.increaseLostStones(enemyPlayer);
            actionBuildsMorris = false;
        } else {
            logger.error("Ungültige GameAction");
        }



        boolean allEnemyStonesInMorris  = boardService.areAllStonesPartOfMorris(board, pairingService.getIndexOfPlayer(pairing, enemyPlayer));
        if (actionBuildsMorris && !allEnemyStonesInMorris){
            playerService.changeToKillPhase(currentPlayer);
            playerService.changeToWaitPhase(enemyPlayer);
        } else {
            playerService.changeToWaitPhase(currentPlayer);
            playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
            pairingService.changeTurn(pairing);
            logger.info("Neuer Aktueller Spieler: " + pairingService.getCurrentPlayer(pairing).getName());
            gameStateService.increaseRound(gameState);
        }
    }



}



