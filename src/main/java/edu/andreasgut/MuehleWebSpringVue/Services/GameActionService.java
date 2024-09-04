package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.BoardRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GameActionService {

    private static final Logger logger = LoggerFactory.getLogger(GameActionService.class);

    private final GameRepository gameRepository;
    private final SenderService senderService;

    @Autowired
    public GameActionService(GameRepository gameRepository, SenderService senderService) {
        this.gameRepository = gameRepository;
        this.senderService = senderService;
    }


    public void handleAction(JsonObject jsonObject, String webSocketSessionId) {
        String type = jsonObject.get("type").getAsString();
        Game gameAfterHumanAction;

        switch (type) {
            case "PUT":
                 gameAfterHumanAction = handlePut(jsonObject, webSocketSessionId);
                 break;
            case "MOVE":
                gameAfterHumanAction =  handleMove(jsonObject, webSocketSessionId);
                break;
            case "KILL":
                gameAfterHumanAction =  handleKill(jsonObject, webSocketSessionId);
                break;
            case "JUMP":
                gameAfterHumanAction =  handleJump(jsonObject, webSocketSessionId);
                break;
            default:
                gameAfterHumanAction = null;
                logger.warn("Ungültiger Action Type");
        }

        GameUpdateDto updateAfterHumanPlayer = new GameUpdateDto(gameAfterHumanAction, LocalDateTime.now());
        gameRepository.save(gameAfterHumanAction);
        senderService.sendGameUpdate(updateAfterHumanPlayer);

        while (gameAfterHumanAction.isCurrentPlayerAComputerPlayer()){
            Game gameAfterComputerAction = triggerComputerPlayer(gameAfterHumanAction);
            GameUpdateDto updateAfterComputer = new GameUpdateDto(gameAfterComputerAction, LocalDateTime.now());
            gameRepository.save(gameAfterComputerAction);
            senderService.sendGameUpdate(updateAfterComputer);
        }
    }



    private Game handlePut(JsonObject jsonObject, String webSocketSessionId) {
        try {
            int ring = jsonObject.get("ring").getAsInt();
            int field = jsonObject.get("field").getAsInt();
            String uuid = jsonObject.get("uuid").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();

            Put put = new Put(uuid, new Position(ring, field));
            Game game = gameRepository.findByGameCode(gameCode);

            if (game.executePut(put, uuid)) {
                gameRepository.save(game);
                logger.info("Put ausgeführt in Game " + gameCode);
                return game;
            } else {
                logger.warn("Ungültige Position bei Put in Game " + gameCode);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Put konnte nicht ausgeführt werden...");
        }

        return null;

    }

    public Game handleMove(JsonObject jsonObject, String webSocketSessionId) {
        try {
            JsonObject fromObject = jsonObject.getAsJsonObject("from");
            int fromRing = fromObject.get("ring").getAsInt();
            int fromField = fromObject.get("field").getAsInt();

            JsonObject toObject = jsonObject.getAsJsonObject("to");
            int toRing = toObject.get("ring").getAsInt();
            int toField = toObject.get("field").getAsInt();

            String uuid = jsonObject.get("uuid").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();

            Move move = new Move(uuid, new Position(fromRing, fromField), new Position(toRing, toField));
            Game game = gameRepository.findByGameCode(gameCode);

            if (game.executeMove(move, uuid)) {
                gameRepository.save(game);
                return game;
            } else {
                logger.warn("Ungültige Position bei Kill in Game " + gameCode);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Kill konnte nicht ausgeführt werden...");
        }

        return null;
    }

    public Game handleKill(JsonObject jsonObject, String webSocketSessionId) {
        try {
            int ring = jsonObject.get("ring").getAsInt();
            int field = jsonObject.get("field").getAsInt();
            String uuid = jsonObject.get("uuid").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();

            Kill kill = new Kill(uuid, new Position(ring, field));
            Game game = gameRepository.findByGameCode(gameCode);

            if (game.executeKill(kill, uuid)) {
                gameRepository.save(game);
                return game;
            } else {
                logger.warn("Ungültige Position bei Kill in Game " + gameCode);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Kill konnte nicht ausgeführt werden...");
        }

        return null;


    }

    public Game handleJump(JsonObject jsonObject, String webSocketSessionId) {
        try {
            JsonObject fromObject = jsonObject.getAsJsonObject("from");
            int fromRing = fromObject.get("ring").getAsInt();
            int fromField = fromObject.get("field").getAsInt();

            JsonObject toObject = jsonObject.getAsJsonObject("to");
            int toRing = toObject.get("ring").getAsInt();
            int toField = toObject.get("field").getAsInt();

            String uuid = jsonObject.get("uuid").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();

            Jump jump = new Jump(uuid, new Position(fromRing, fromField), new Position(toRing, toField));
            Game game = gameRepository.findByGameCode(gameCode);

            if (game.executeJump(jump, uuid)) {
                gameRepository.save(game);
                return game;
            } else {
                logger.warn("Ungültige Position bei Jump in Game " + gameCode);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn("Jump konnte nicht ausgeführt werden...");
        }

        return null;
    }

    private Game triggerComputerPlayer(Game game){
        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) game.getPairing().getCurrentPlayer();
        PHASE phase = standardComputerPlayer.getCurrentPhase();
        int index = game.getPairing().getCurrentPlayerIndex();
        String uuid = standardComputerPlayer.getUuid();
        switch (phase){
            case PUT:
                Put put = standardComputerPlayer.put(game, uuid);
                game.executePut(put, uuid);
                break;
            case MOVE:
                standardComputerPlayer.move(game, uuid);
                break;
            case KILL:
                Kill kill = standardComputerPlayer.kill(game, uuid);
                game.executeKill(kill, uuid);
                break;
            case JUMP:
                standardComputerPlayer.jump(game, uuid);
                break;
        }
        return game;
    }

}



