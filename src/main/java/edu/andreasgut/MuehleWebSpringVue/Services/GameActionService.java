package edu.andreasgut.MuehleWebSpringVue.Services;

import com.google.gson.JsonObject;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
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

        switch (type){
            case "put":
                return handlePut(jsonObject, webSocketSessionId);
            case "move":
                //handleMove(jsonObject, webSocketSessionId);
                return null;
            case "kill":
                //handleKill(jsonObject, webSocketSessionId);
                return null;
            default:
                return null;

        }
    }

    private Game handlePut(JsonObject jsonObject, String webSocketSessionId){
        try {
            int ring = jsonObject.get("ring").getAsInt();
            int field = jsonObject.get("field").getAsInt();
            String uuid = jsonObject.get("uuid").getAsString();
            String gameCode = jsonObject.get("gamecode").getAsString();

            if (isPutValid(jsonObject, webSocketSessionId)){
                Put put = new Put(uuid, new Position(ring, field));
                Game game = gameRepository.findByGameCode(gameCode);
                Pairing pairing = game.getPairing();
                Board board = game.getBoard();
                int activePlayerIndex = pairing.getPlayerIndexByPlayerUuid(uuid);
                int passivePlayerIndex = activePlayerIndex == 1 ? 2 : 1;
                board.putStone(put.getPutPosition(), activePlayerIndex);

                boolean morrisDetected = false;
                if (morrisDetected){
                    pairing.getPlayerByIndex(activePlayerIndex).setCurrentPhase(PHASE.KILL);
                    pairing.getPlayerByIndex(passivePlayerIndex).setCurrentPhase(PHASE.WAIT);

                } else {
                    pairing.getPlayerByIndex(activePlayerIndex).setCurrentPhase(PHASE.WAIT);
                    //TODO: Hier muss die wirkliche PHASE ermittelt werden
                    pairing.getPlayerByIndex(passivePlayerIndex).setCurrentPhase(PHASE.SET);
                }
                game.increaseRound();
                game.getPairing().getPlayerByPlayerUuid(uuid).increaseNumberOfStonesPut();
                gameRepository.save(game);
                return game;
            } else {
                logger.warn("Ungültige Position bei Put in Game " + gameCode);
                return null;
            }
        } catch (Exception e){
            logger.warn("Put konnte nicht ausgeführt werden...");
        }

        return null;

    }

    private boolean isPutValid(JsonObject jsonObject, String webSocketSessionId) {
        try {
            int ring = jsonObject.get("ring").getAsInt();
            int field = jsonObject.get("field").getAsInt();
            String gameCode = jsonObject.get("gamecode").getAsString();
            Game game = gameRepository.findByGameCode(gameCode);
            String uuid = jsonObject.get("uuid").getAsString();
            boolean phaseOk = game.getPairing().getCurrentPlayer().getCurrentPhase() == PHASE.SET;
            boolean turnOk = game.getPairing().getCurrentPlayer().getUuid().equals(uuid);
            boolean positionOk = game.getBoard().getStateOfPosition(new Position(ring, field)) == POSITIONSTATE.FREE;

            return phaseOk /*&& turnOk*/ && positionOk;
        } catch (Exception e) {
            logger.warn("Put konnte nicht ausgeführt werden...");
        }

        return false;
    }
}




