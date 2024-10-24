package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.DTO.GameSetupDto;
import edu.andreasgut.MuehleWebSpringVue.DTO.GameUpdateDto;
import edu.andreasgut.MuehleWebSpringVue.DTO.StatisticsDto;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderService {

    SimpMessagingTemplate template;
    GameRepository gameRepository;
    private static final Logger logger = LoggerFactory.getLogger(PairingService.class);

    @Autowired
    public SenderService(SimpMessagingTemplate template, GameRepository gameRepository) {
        this.template = template;
        this.gameRepository = gameRepository;
    }

    public void sendUpdateGameToAdmin(GameUpdateDto gameUpdateDto){
        String path = "/topic/admin/games/update";
        template.convertAndSend(path, gameUpdateDto);
    }

    public void sendAddGameToAdmin(GameSetupDto gameSetupDto){
        String path = "/topic/admin/games/add";
        template.convertAndSend(path, gameSetupDto);
    }


    public void sendGetAllGames(){
        String path = "/topic/admin/games/getall";
        template.convertAndSend(path, gameRepository.findAll());
    }



    public void sendGameUpdate(GameUpdateDto gameUpdateDto){
        sendUpdateGameToAdmin(gameUpdateDto);
        logger.info("Update in Game " + gameUpdateDto.getGame().getGameCode() + " versandt.");
        String gameCode = gameUpdateDto.getGame().getGameCode();
        String path = "/topic/game/" + gameCode + "/gameupdate";
        System.out.println(gameUpdateDto);
        template.convertAndSend(path, gameUpdateDto);
    }

    public void sendSecondPlayer(Player player, String gameCode){
        String path = "/topic/game/" + gameCode + "/secondplayer";
        System.out.println(player);
        template.convertAndSend(path, player);
    }

    public void sendStatisticUpdate(StatisticsDto statisticsDto){
        String path = "/topic/statistics/getall";
        template.convertAndSend(path, statisticsDto);
    }



}
