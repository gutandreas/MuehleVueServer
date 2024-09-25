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


        POSITIONSTATE[][] positionstates = new POSITIONSTATE[3][8];

        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                positionstates[ring][field] = POSITIONSTATE.FREE;

            }

        }
        positionstates[0][2] = POSITIONSTATE.PLAYER1;
        positionstates[2][2] = POSITIONSTATE.PLAYER1;
        positionstates[1][4] = POSITIONSTATE.PLAYER1;
        positionstates[0][7] = POSITIONSTATE.PLAYER1;
        positionstates[1][2] = POSITIONSTATE.PLAYER2;
        positionstates[2][5] = POSITIONSTATE.PLAYER2;
        positionstates[0][4] = POSITIONSTATE.PLAYER2;
        positionstates[2][7] = POSITIONSTATE.PLAYER2;
        Board board = new Board(positionstates);

        System.out.println(board);
    }
}
