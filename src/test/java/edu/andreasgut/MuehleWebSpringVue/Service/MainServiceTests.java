package edu.andreasgut.MuehleWebSpringVue.Service;

import edu.andreasgut.MuehleWebSpringVue.Repositories.PlayerRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MainServiceTests {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    public void MainService_HandlePut(){


    }
}
