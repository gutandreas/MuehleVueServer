package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.GameState;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import org.springframework.stereotype.Service;

@Service
public class GameStateService {

    public void increaseRound(GameState gameState){
        gameState.setRound(gameState.getRound()+1);
    }

    public void finishGame(GameState gameState) {
        gameState.setFinished(true);
    }

    public boolean isGameFinished(GameState gameState) { return gameState.isFinished();}

    public int getRound(GameState gameState) { return  gameState.getRound();}

    public void setWinner(GameState gameState, int winnerIndex) {
        gameState.setWinnerIndex(winnerIndex);
    }

    public int getWinnerIndex(GameState gameState){
        return gameState.getWinnerIndex();
    }


}
