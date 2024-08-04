package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class GameManagerService {

    @Autowired
    GameRepository gameRepository;

    public LinkedList<Game> getActiveGames(){
        LinkedList<Game> games = gameRepository.findByFinishedFalse();
        return games;
    }
}
