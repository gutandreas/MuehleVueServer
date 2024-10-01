package edu.andreasgut.MuehleWebSpringVue.Service;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.POSITIONSTATE;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;
import edu.andreasgut.MuehleWebSpringVue.Repositories.BoardRepository;
import edu.andreasgut.MuehleWebSpringVue.Services.BoardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BoardServiceTests {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;


    @Test
    public void BoardService_MoveStone(){

        Board board = new Board();

        when(boardRepository.save(any())).thenReturn(null);

        boardService.putStoneAndSave(board, new Put(new Position(1,1)), 1);
        boardService.moveStoneAndSave(board, new Move(new Position(1, 1), new Position(1, 2)), 1);

        POSITIONSTATE positionstate = boardService.getStateOfPosition(board, new Position(1, 2));

        Assertions.assertEquals(POSITIONSTATE.PLAYER1, positionstate);
        boardService.moveStoneAndSave(board, new Move(new Position(1, 2), new Position(1, 1)), 1);

        positionstate = boardService.getStateOfPosition(board, new Position(1, 1));

        Assertions.assertEquals(POSITIONSTATE.PLAYER1, positionstate);

    }

    @Test
    public void BoardService_KillStone(){

        Board board = new Board();

        when(boardRepository.save(any())).thenReturn(null);

        boardService.putStoneAndSave(board, new Put(new Position(1,1)), 1);
        boardService.putStoneAndSave(board, new Put(new Position(1,2)), 1);
        boardService.killStone(board, new Kill(new Position(1, 1)));

        POSITIONSTATE freePositionState = boardService.getStateOfPosition(board, new Position(1, 1));
        POSITIONSTATE player1PositionState = boardService.getStateOfPosition(board, new Position(1, 2));

        Assertions.assertEquals(POSITIONSTATE.FREE, freePositionState);
        Assertions.assertEquals(POSITIONSTATE.PLAYER1, player1PositionState);

    }
}
