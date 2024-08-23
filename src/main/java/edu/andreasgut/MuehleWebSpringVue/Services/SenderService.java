package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderService {

    SimpMessagingTemplate template;
    GameRepository gameRepository;

    @Autowired
    public SenderService(SimpMessagingTemplate template, GameRepository gameRepository) {
        this.template = template;
        this.gameRepository = gameRepository;
    }

    public void sendUpdateGameToAdmin(Game game){
        String path = "/topic/admin/games/update";
        template.convertAndSend(path, game);
    }

    public void sendAddGameToAdmin(Game game){
        String path = "/topic/admin/games/add";
        template.convertAndSend(path, game);
    }


    public void sendGetAllGames(){
        String path = "/topic/admin/games/getall";
        template.convertAndSend(path, gameRepository.findAll());
    }


    public void sendGameUpdate(GameUpdateDto gameUpdateDto){
        String path = "/topic/game/gameupdate";
        System.out.println(gameUpdateDto);
        template.convertAndSend(path, gameUpdateDto);
    }



}
