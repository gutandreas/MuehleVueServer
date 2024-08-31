package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Repositories.BoardRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameActionService {

    private static final Logger logger = LoggerFactory.getLogger(GameActionService.class);

    private final GameRepository gameRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public GameActionService(GameRepository gameRepository, BoardRepository boardRepository) {
        this.gameRepository = gameRepository;
        this.boardRepository = boardRepository;
    }


    public Game handleAction(JsonObject jsonObject, String webSocketSessionId) {
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "PUT":
                return handlePut(jsonObject, webSocketSessionId);
            case "MOVE":
                return handleMove(jsonObject, webSocketSessionId);
            case "KILL":
                return handleKill(jsonObject, webSocketSessionId);
            case "JUMP":
                return handleJump(jsonObject, webSocketSessionId);
            default:
                logger.warn("Ungültiger Action Type");
                return null;

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

}



