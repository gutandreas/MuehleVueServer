package edu.andreasgut.MuehleWebSpringVue.Services;


import edu.andreasgut.MuehleWebSpringVue.DTO.*;
import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;



@Service
public class GameServices {

    @Autowired
    GameRepository gameRepository;

    Map<String, Game> gameMap = new HashMap<>();

    public GameDto setupComputerGame(JsonObject jsonRequest){
        STONECOLOR playerStonecolor = jsonRequest.get("color").toString().equals("BLACK") ? STONECOLOR.BLACK : STONECOLOR.WHITE;
        String firstStone = jsonRequest.get("firststone").toString();
        int startPlayerIndex = firstStone.equals("e") ? 1 : 2;


        int level = jsonRequest.get("level").getAsInt();
        String computerName;
        if (level == 0){
            computerName = "Schwacher Computer";
        } else if (level == 1) {
            computerName = "Mittelstarker Computer";
        } else if (level == 2) {
            computerName = "Starker Computer";
        } else {
            computerName = "Computer (ungültiges Level)";
        }
        STONECOLOR computerStonecolor = playerStonecolor == STONECOLOR.BLACK ? STONECOLOR.WHITE : STONECOLOR.BLACK;

        Player humanPlayer = new HumanPlayer(jsonRequest.get("name").toString(), playerStonecolor);
        Player computerPlayer = new StandardComputerPlayer(computerName, computerStonecolor, level);
        Pairing pairing = new Pairing(humanPlayer, computerPlayer, startPlayerIndex);
        String gameCode = generateRandomFreeGameCode();

        Game game = new Game(gameCode, new Board(), pairing, 0);

        if (addGame(game)){
            PlayerOwnDto ownPlayerDto = new PlayerOwnDto(humanPlayer.getName(), humanPlayer.getStonecolor(), humanPlayer.getPlayerId());
            PlayerEnemyDto enemyPlayerDto = new PlayerEnemyDto(computerPlayer.getName(), computerPlayer.getStonecolor());
            PairingDto pairingDto = new PairingDto(ownPlayerDto, enemyPlayerDto, startPlayerIndex);
            GameDto gameDto = new GameDto(pairingDto, new BoardDto());
            addGameToDatabase(game);
            getGameFromDatabase(gameCode);

            return gameDto;
        };

        return null;



    }

    public boolean doesGameExist(Game game){
        return gameMap.containsKey(game.getGameCode());
    }

    public boolean doesGameCodeExist(String gameCode){
        return gameMap.containsKey(gameCode);
    }

    private boolean addGame(Game game){
        if (doesGameExist(game)){
            System.out.println("Game mit Gamecode " + game.getGameCode() + " existiert bereits und kann nicht hinzugefügt werden");
            return false;
        } else {
            gameMap.put(game.getGameCode(), game);
            System.out.println("Game mit Gamecode " + game.getGameCode() + " hinzugefügt");
            return true;
        }
    }

    private Game getGame(Game game){
        return gameMap.get(game.getGameCode());
    }

    private boolean deleteGame(Game game){
        if (doesGameExist(game)){
            gameMap.remove(game.getGameCode());
            System.out.println("Game mit Gamecode " + game.getGameCode() + " wurde gelöscht");
            return true;
        } else {
            System.out.println("Game mit Gamecode " + game.getGameCode() + " existiert nicht und kann nicht gelöscht werden");
            return false;
        }
    }

    private void addGameToDatabase(Game game){
        gameRepository.save(game);
    }

    private void getGameFromDatabase(String gameCode){
        System.out.println("Game aus Datenbank: " + gameRepository.findByGameCode(gameCode));
    }

    private String generateRandomFreeGameCode(){
        String gameCode;
        do {
            gameCode = generateRandomStringOfLength6();
        } while (doesGameCodeExist(gameCode));

        return gameCode;
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
