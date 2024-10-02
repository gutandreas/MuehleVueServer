package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.DTO.StatisticsDto;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.GameRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.HumanPlayerRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.SpectatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class StatisticService {


    private GameRepository gameRepository;
    private HumanPlayerRepository humanPlayerRepository;
    private SpectatorRepository spectatorRepository;
    private static final Logger logger = LoggerFactory.getLogger(StatisticService.class);

    public StatisticService(GameRepository gameRepository, HumanPlayerRepository humanPlayerRepository, SpectatorRepository spectatorRepository) {
        this.gameRepository = gameRepository;
        this.humanPlayerRepository = humanPlayerRepository;
        this.spectatorRepository = spectatorRepository;
    }

    public StatisticsDto getUpdatedStatistic(){
        int numberOfActiveGames = gameRepository.findByGameState_FinishedFalse().size();
        int numberOfGamesTotal = gameRepository.findAll().size();
        int numberOfHumanPlayers = humanPlayerRepository.findAll().size();
        int numberOfSpectators = spectatorRepository.findAll().size();
        LinkedList<HumanPlayer> activeHumanPlayers = humanPlayerRepository.findAllWithUnfinishedGames();
        int numberOfFinishedGamesWithComputer = gameRepository.countFinishedGamesByStandardComputerPlayer();
        int numberOfGamesWonByComputer = gameRepository.countGamesWonByStandardComputerPlayer();
        logger.info("Statistiken abgefragt...");
        return  new StatisticsDto(numberOfActiveGames, numberOfGamesTotal, numberOfHumanPlayers, activeHumanPlayers, numberOfSpectators, numberOfFinishedGamesWithComputer, numberOfGamesWonByComputer);
    }


}
