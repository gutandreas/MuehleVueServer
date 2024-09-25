package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Services.BoardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardTest {

    @Mock
    BoardService boardService;

    @Test
    public void Board_toString(){

        Board board = new Board();

        boardService.putStone(board, new Put(new Position(0, 6)), 1);
        boardService.putStone(board, new Put(new Position(2, 2)), 1);
        boardService.putStone(board, new Put(new Position(1, 4)), 1);
        boardService.putStone(board, new Put(new Position(1, 6)), 2);
        boardService.putStone(board, new Put(new Position(0, 2)), 2);
        boardService.putStone(board, new Put(new Position(2, 4)), 2);
        System.out.println(board);
    }
}
