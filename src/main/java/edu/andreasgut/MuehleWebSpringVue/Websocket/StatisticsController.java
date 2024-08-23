package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.DTO.StatisticsDto;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.HumanPlayerRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.SpectatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);


    GameRepository gameRepository;
    HumanPlayerRepository humanPlayerRepository;
    SpectatorRepository spectatorRepository;

    public StatisticsController(GameRepository gameRepository, HumanPlayerRepository humanPlayerRepository, SpectatorRepository spectatorRepository) {
        this.gameRepository = gameRepository;
        this.humanPlayerRepository = humanPlayerRepository;
        this.spectatorRepository = spectatorRepository;
    }

    @MessageMapping("/statistics/getall")
    @SendTo("/topic/statistics/getall")
    public StatisticsDto handleMessage() {
        int numberOfActiveGames = gameRepository.findByFinishedFalse().size();
        int numberOfGamesTotal = gameRepository.findAll().size();
        int numberOfHumanPlayers = humanPlayerRepository.findAll().size();
        int numberOfSpectators = spectatorRepository.findAll().size();
        logger.info("Statistiken abgefragt...");

        return new StatisticsDto(numberOfActiveGames, numberOfGamesTotal, numberOfHumanPlayers, numberOfSpectators);
    }
}
