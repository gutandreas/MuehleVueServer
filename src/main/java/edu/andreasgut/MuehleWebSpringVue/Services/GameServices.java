package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.DTO.*;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.GameAction;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.JsonObject;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameServices {

    private final GameRepository gameRepository;

    private final Map<String, Game> gameMap = new HashMap<>();

    // Konstruktor-Injektion
    public GameServices(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameDto setupLoginGame(JsonObject jsonRequest, WebSocketSession webSocketSession) {

        String startOrJoin = jsonRequest.get("join").getAsString();
        String gameCode = jsonRequest.get("gamecode").getAsString();

        switch (startOrJoin) {
            case "s":
                System.out.println(getClass().getSimpleName() + "- Neues Logingame (start) erstellt");
                STONECOLOR playerStonecolor = jsonRequest.get("color").toString().equals("BLACK") ? STONECOLOR.BLACK : STONECOLOR.WHITE;
                String firstStone = jsonRequest.get("firststone").toString();
                int startPlayerIndex = firstStone.equals("e") ? 1 : 2;

                Player humanPlayerStart = new HumanPlayer(jsonRequest.get("name").toString(), playerStonecolor, webSocketSession.getId());
                Pairing pairing = new Pairing(humanPlayerStart, startPlayerIndex);
                Game gameStart = new Game(gameCode, new Board(), pairing, 0);

                if (addGame(gameStart)) {
                    PlayerOwnDto ownPlayerDto = new PlayerOwnDto(humanPlayerStart.getName(), humanPlayerStart.getStonecolor(), humanPlayerStart.getUuid());
                    PairingDto pairingDto = new PairingDto(ownPlayerDto, startPlayerIndex);
                    GameDto gameDto = new GameDto(pairingDto, new BoardDto());
                    addGameToDatabase(gameStart);
                    getGameFromDatabase(gameCode);

                    return gameDto;
                }
                break;

            case "j":
                System.out.println(getClass().getSimpleName() + "- Logingame (join) beigetreten");

                Game gameJoin = gameMap.get(gameCode);
                STONECOLOR playerStonecolorJoin = gameJoin.getPairing().getPlayer1().getStonecolor() == STONECOLOR.BLACK ? STONECOLOR.WHITE : STONECOLOR.BLACK;

                Player humanPlayerJoin = new HumanPlayer(jsonRequest.get("name").toString(), playerStonecolorJoin, webSocketSession.getId());
                gameJoin.getPairing().addSecondPlayer(humanPlayerJoin);

                addGameToDatabase(gameJoin);
                getGameFromDatabase(gameCode);

                return null; // TODO: muss noch durch GameDto ersetzt werden
        }

        return null;
    }

    public boolean doesGameExist(Game game) {
        return gameMap.containsKey(game.getGameCode());
    }

    public boolean doesGameCodeExist(String gameCode) {
        return gameMap.containsKey(gameCode);
    }

    private boolean addGame(Game game) {
        if (doesGameExist(game)) {
            System.out.println("Game mit Gamecode " + game.getGameCode() + " existiert bereits und kann nicht hinzugefügt werden");
            return false;
        } else {
            gameMap.put(game.getGameCode(), game);
            System.out.println("Game mit Gamecode " + game.getGameCode() + " hinzugefügt");
            return true;
        }
    }

    private Game getGame(Game game) {
        return gameMap.get(game.getGameCode());
    }

    private boolean deleteGame(Game game) {
        if (doesGameExist(game)) {
            gameMap.remove(game.getGameCode());
            System.out.println("Game mit Gamecode " + game.getGameCode() + " wurde gelöscht");
            return true;
        } else {
            System.out.println("Game mit Gamecode " + game.getGameCode() + " existiert nicht und kann nicht gelöscht werden");
            return false;
        }
    }

    private void addGameToDatabase(Game game) {
        gameRepository.save(game);
    }

    private void getGameFromDatabase(String gameCode) {
        gameRepository.findByGameCode(gameCode);
    }

    private String generateRandomFreeGameCode() {
        String gameCode;
        do {
            gameCode = generateRandomStringOfLength6();
        } while (doesGameCodeExist(gameCode));

        return gameCode;
    }

    public void handlePut(String gameCode, String playerUuid, Put put, WebSocketSession webSocketSession) {
        boolean putValid = isItPlayersTurn(gameCode, webSocketSession)
                && isActionValidInGamePhase(gameCode, put, webSocketSession);

        if (putValid) {
            Game game = gameMap.get(gameCode);
            int playerIndex = game.getPairing().getPlayerIndexByPlayerUuid(playerUuid);
            game.getBoard().putStone(put.getPutPosition(), playerIndex);
        }
    }

    public void handleMove(String gameCode, String playerUuid, Move move, WebSocketSession webSocketSession) {
        boolean moveValid = isItPlayersTurn(gameCode, webSocketSession)
                && isActionValidInGamePhase(gameCode, move, webSocketSession);

        if (moveValid) {
            Game game = gameMap.get(gameCode);
            int playerIndex = game.getPairing().getPlayerIndexByPlayerUuid(playerUuid);
            game.getBoard().moveStone(move, playerIndex);
        }
    }

    public void handleKill(String gameCode, String playerUuid, Kill kill, WebSocketSession webSocketSession) {
        boolean killValid = isItPlayersTurn(gameCode, webSocketSession)
                && isActionValidInGamePhase(gameCode, kill, webSocketSession);

        if (killValid) {
            Game game = gameMap.get(gameCode);
            game.getBoard().killStone(kill.getKillPosition());
        }
    }

    private boolean isActionValidInGamePhase(String gameCode, GameAction gameAction, WebSocketSession webSocketSession) {
        Game game = gameMap.get(gameCode);
        if (gameAction instanceof Put) {
            return game.getPairing().getCurrentPlayer().getCurrentPhase() == PHASE.SET;
        }
        if (gameAction instanceof Move) {
            return game.getPairing().getCurrentPlayer().getCurrentPhase() == PHASE.MOVE;
        }
        return true;
    }

    private boolean isItPlayersTurn(String gameCode, WebSocketSession webSocketSession) {
        Game game = gameMap.get(gameCode);
        return webSocketSession.getId().equals(((HumanPlayer) game.getPairing().getCurrentPlayer()).getWebSocketSessionId());
    }

    private static String generateRandomStringOfLength6() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
