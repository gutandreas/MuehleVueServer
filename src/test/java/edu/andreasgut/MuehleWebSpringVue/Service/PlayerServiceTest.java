package edu.andreasgut.MuehleWebSpringVue.Service;

import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.STONECOLOR;
import edu.andreasgut.MuehleWebSpringVue.Repositories.PlayerRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    public void PlayerService_IcreasePutStones_Returns9(){
        Player player = new Player("Hansli", STONECOLOR.WHITE, PHASE.PUT);
        for (int i = 0; i < 9; i++) {
            playerService.increasePutStones(player);
        }

        Assertions.assertEquals(9, player.getNumberOfStonesPut());


    }
}
