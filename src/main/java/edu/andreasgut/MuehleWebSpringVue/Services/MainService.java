package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
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

    @Autowired
    public MainService(GameRepository gameRepository, SenderService senderService, BoardService boardService, PairingService pairingService, PlayerService playerService, GameService gameService, GameStateService gameStateService) {
        this.gameRepository = gameRepository;
        this.senderService = senderService;
        this.boardService = boardService;
        this.pairingService = pairingService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.gameStateService = gameStateService;
    }

    public void handleAction(JsonObject jsonObject, String webSocketSessionId) {
        String type = jsonObject.get("type").getAsString();
        String gameCode = jsonObject.get("gameCode").getAsString();
        GameState gameStateAfterHumanAction;

        switch (type) {
            case "PUT":
                 handlePut(jsonObject);
                 break;
            case "MOVE":
                //handleMove(jsonObject);
                break;
            case "KILL":
                //handleKill(jsonObject);
                break;
            case "JUMP":
                //handleJump(jsonObject);
                break;
            default:
                logger.warn("Ungültiger Action Type");
        }

        GameUpdateDto updateAfterHumanPlayer = new GameUpdateDto(gameRepository.findByGameCode(gameCode), LocalDateTime.now());
        senderService.sendGameUpdate(updateAfterHumanPlayer);

        /*while (gameStateAfterHumanAction.isCurrentPlayerAComputerPlayer() && !gameStateAfterHumanAction.isFinished()){
            GameState gameAfterComputerAction = triggerComputerPlayer(gameStateAfterHumanAction);
            GameUpdateDto updateAfterComputer = new GameUpdateDto(gameAfterComputerAction, LocalDateTime.now());
            gameRepository.save(gameAfterComputerAction);
            senderService.sendGameUpdate(updateAfterComputer);
        }*/
    }

    private void handlePut(JsonObject jsonObject) {

        String gameCode = jsonObject.get("gamecode").getAsString();
        GamePersistent game = gameRepository.findByGameCode(gameCode);

        try {
            int ring = jsonObject.get("ring").getAsInt();
            int field = jsonObject.get("field").getAsInt();
            String uuid = jsonObject.get("uuid").getAsString();

            Put put = new Put(new Position(ring, field));
            Pairing pairing = game.getPairing();
            Player currentPlayer = pairingService.getCurrentPlayer(pairing);
            Player enemyPlayer = pairingService.getEnemyOf(pairing, currentPlayer);
            Board board = game.getBoard();
            GameState gameState = game.getGameState();

            boolean phaseOK = playerService.isPlayerInPutPhase(currentPlayer);
            boolean uuidOK = pairingService.getPlayerByPlayerUuid(pairing, uuid).equals(pairingService.getCurrentPlayer(pairing));
            boolean positionOK = boardService.isPositionFree(board, put.getPutPosition());

            if (phaseOK && uuidOK && positionOK){
                boardService.putStone(board, put.getPutPosition(), pairingService.getCurrentPlayerIndex(pairing));
                logger.info("Put ausgeführt in GameState " + gameCode);
                updateStatesAfterPut(gameState, board, pairing, currentPlayer, enemyPlayer, put);
            } else {
                logger.warn("Ungültige Position, Phase oder UUID bei Put in GameState " + gameCode);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Put konnte nicht ausgeführt werden...");
        }

        senderService.sendGameUpdate(new GameUpdateDto(game, LocalDateTime.now()));

    }

    public GameState handleMove(JsonObject jsonObject, String webSocketSessionId) {
        /*try {
            JsonObject fromObject = jsonObject.getAsJsonObject("from");
            int fromRing = fromObject.get("ring").getAsInt();
            int fromField = fromObject.get("field").getAsInt();

            JsonObject toObject = jsonObject.getAsJsonObject("to");
            int toRing = toObject.get("ring").getAsInt();
            int toField = toObject.get("field").getAsInt();

            String uuid = jsonObject.get("uuid").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();

            Move move = new Move(new Position(fromRing, fromField), new Position(toRing, toField));
            GameState gameState = gameRepository.findByGameCode(gameCode);



            if (gameState.executeMove(move, uuid)) {

                return gameState;
            } else {
                logger.warn("Ungültige Position bei Kill in GameState " + gameCode);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Kill konnte nicht ausgeführt werden...");
        }*/

        return null;
    }

    public GameState handleKill(JsonObject jsonObject, String webSocketSessionId) {
        /*try {
            int ring = jsonObject.get("ring").getAsInt();
            int field = jsonObject.get("field").getAsInt();
            String uuid = jsonObject.get("uuid").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();

            Kill kill = new Kill(new Position(ring, field));
            GameState gameState = gameRepository.findByGameCode(gameCode);

            if (gameState.executeKill(kill, uuid)) {

                return gameState;
            } else {
                logger.warn("Ungültige Position bei Kill in GameState " + gameCode);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Kill konnte nicht ausgeführt werden...");
        }*/

        return null;


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

    private void updateStatesAfterPut(GameState gameState, Board board, Pairing pairing, Player currentPlayer, Player enemyPlayer, Put put){
        boolean putBuildsMorris = boardService.isPositionPartOfMorris(board, put.getPutPosition());
        boolean allEnemyStonesInMorris  = boardService.areAllStonesPartOfMorris(board, pairingService.getIndexOfPlayer(pairing, enemyPlayer));
        if (putBuildsMorris && !allEnemyStonesInMorris){
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



