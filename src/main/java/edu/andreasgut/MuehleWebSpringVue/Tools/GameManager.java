package edu.andreasgut.MuehleWebSpringVue.Tools;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;

import java.util.HashMap;
import java.util.Map;

public class GameManager {

    private static Map<String, Game> gameMap = new HashMap<>();

    public boolean doesGameExist(Game game){
        return gameMap.containsKey(game.getGameCode());
    }

    public boolean addGame(Game game){
        if (doesGameExist(game)){
            return false;
        } else {
            gameMap.put(game.getGameCode(), game);
            return true;
        }
    }

    public Game getGame(Game game){
        return gameMap.get(game.getGameCode());
    }




}
