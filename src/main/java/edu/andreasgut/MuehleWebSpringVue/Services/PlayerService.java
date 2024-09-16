package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Repositories.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public boolean isPlayerInPutPhase(Player player){
        return  player.getCurrentPhase() == PHASE.PUT;
    }

    public boolean isPlayerInMovePhase(Player player){
        return  player.getCurrentPhase() == PHASE.MOVE;
    }

    public boolean isPlayerInKillPhase(Player player){
        return  player.getCurrentPhase() == PHASE.KILL;
    }

    public boolean isPlayerInJumpPhase(Player player){
        return  player.getCurrentPhase() == PHASE.JUMP;
    }

    public boolean isPlayerInWaitPhase(Player player){
        return  player.getCurrentPhase() == PHASE.WAIT;
    }

    public void changeToPutPhase(Player player) { player.setCurrentPhase(PHASE.PUT);}
    public void changeToMovePhase(Player player) { player.setCurrentPhase(PHASE.MOVE);}
    public void changeToKillPhase(Player player) { player.setCurrentPhase(PHASE.KILL);}
    public void changeToJumpPhase(Player player) { player.setCurrentPhase(PHASE.JUMP);}
    public void changeToWaitPhase(Player player) { player.setCurrentPhase(PHASE.WAIT);}

    public PHASE getPhase(Player player) {return player.getCurrentPhase();}

    public int getNumerOfPutStones(Player player){
        return player.getNumberOfStonesPut();
    }

    public int getNumerOfLostStones(Player player){
        return player.getNumberOfStonesLost();
    }

    public int getNumerOfKilledStones(Player player){
        return player.getNumberOfStonesKilled();
    }

    @Transactional
    public void increasePutStones(Player player){
        player.setNumberOfStonesPut(player.getNumberOfStonesPut()+1);
        savePlayer(player);
    }

    @Transactional
    public void increaseLostStones(Player player){
        player.setNumberOfStonesLost(player.getNumberOfStonesLost()+1);
        savePlayer(player);
    }

    @Transactional
    public void increaseKilledStones(Player player){
        player.setNumberOfStonesKilled(player.getNumberOfStonesKilled()+1);
        savePlayer(player);
    }

    private void savePlayer(Player player) {
        playerRepository.save(player);
    }

}
