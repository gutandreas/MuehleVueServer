package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Random;

@Service
public class GameManagerService {

    @Autowired
    GameRepository gameRepository;

    public LinkedList<Game> getActiveGames(){
        LinkedList<Game> games = gameRepository.findByFinishedFalse();
        return games;
    }

    public boolean doesGameExist(String gameCode){
        return gameRepository.findByGameCode(gameCode).size() > 0;
    }

    public void deleteGameByGameCode(String gameCode){
        gameRepository.deleteGameByGameCode(gameCode);
    }

    public String generateValidGameCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder gameCode = new StringBuilder();
        Random random = new Random();

        do {

            for (int i = 0; i < 6; i++) {
                int index = random.nextInt(characters.length());
                gameCode.append(characters.charAt(index));
            }
        } while (gameRepository.existsByGameCode(gameCode.toString()));

        return gameCode.toString();
    }
}
