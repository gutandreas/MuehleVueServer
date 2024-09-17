package edu.andreasgut.MuehleWebSpringVue.Service;

import edu.andreasgut.MuehleWebSpringVue.Models.*;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Repositories.BoardRepository;
import edu.andreasgut.MuehleWebSpringVue.Repositories.PlayerRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.BoardService;
import edu.andreasgut.MuehleWebSpringVue.Services.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainServiceTests {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    public void MainService_HandleMove(){

        Board board = new Board();

        when(boardRepository.save(any())).thenReturn(null);

        boardService.putStone(board, new Put(new Position(1,1)), 1);
        boardService.moveStone(board, new Move(new Position(1, 1), new Position(1, 2)), 1);

        POSITIONSTATE positionstate = boardService.getStateOfPosition(board, new Position(1, 2));

        Assertions.assertEquals(POSITIONSTATE.PLAYER1, positionstate);
        boardService.moveStone(board, new Move(new Position(1, 2), new Position(1, 1)), 1);

        positionstate = boardService.getStateOfPosition(board, new Position(1, 1));

        Assertions.assertEquals(POSITIONSTATE.PLAYER1, positionstate);

    }
}
