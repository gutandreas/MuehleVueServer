package edu.andreasgut.MuehleWebSpringVue.Websocket;

import edu.andreasgut.MuehleWebSpringVue.DTO.StatisticsDto;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.HumanPlayerRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.SpectatorRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.SenderService;
import edu.andreasgut.MuehleWebSpringVue.Services.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);


    GameRepository gameRepository;
    HumanPlayerRepository humanPlayerRepository;
    SpectatorRepository spectatorRepository;
    SenderService senderService;
    StatisticService statisticService;

    public StatisticsController(GameRepository gameRepository, HumanPlayerRepository humanPlayerRepository, SpectatorRepository spectatorRepository, SenderService senderService, StatisticService statisticService) {
        this.gameRepository = gameRepository;
        this.humanPlayerRepository = humanPlayerRepository;
        this.spectatorRepository = spectatorRepository;
        this.senderService = senderService;
        this.statisticService = statisticService;
    }

    @MessageMapping("/statistics/getall")
    public void handleMessage() {
        StatisticsDto statisticsDto = statisticService.getUpdatedStatistic();
        senderService.sendStatisticUpdate(statisticsDto);
    }
}
