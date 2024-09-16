package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    BoardService boardService;

    public GameService(BoardService boardService) {
        this.boardService = boardService;
    }



    public void addSpectator(Game game, Spectator spectator){
        game.getSpectators().add(spectator);
    }


}
