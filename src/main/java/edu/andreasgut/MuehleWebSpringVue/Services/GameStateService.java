package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.GameState;
import org.springframework.stereotype.Service;

@Service
public class GameStateService {

    public void increaseRound(GameState gameState){
        gameState.setRound(gameState.getRound()+1);
    }

    public void finishGame(GameState gameState) {
        gameState.setFinished(true);
    }


}
