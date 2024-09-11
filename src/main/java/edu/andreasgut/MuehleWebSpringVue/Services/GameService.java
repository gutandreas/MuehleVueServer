package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.GamePersistent;
import edu.andreasgut.MuehleWebSpringVue.Models.GameState;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    BoardService boardService;

    public GameService(BoardService boardService) {
        this.boardService = boardService;
    }



    public void addSpectator(GamePersistent game, Spectator spectator){
        game.getSpectators().add(spectator);
    }


}
