package edu.andreasgut.MuehleWebSpringVue.Models;


import edu.andreasgut.MuehleWebSpringVue.Converter.BoardArrayConverter;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;




@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID boardUuid;

    @Convert(converter = BoardArrayConverter.class)
    POSITIONSTATE[][] boardPositionsStates;

    public Board() {
        boardPositionsStates = new POSITIONSTATE[3][8];
        for (int i = 0; i < boardPositionsStates.length; i++) {
            for (int j = 0; j < boardPositionsStates[0].length; j++) {
                boardPositionsStates[i][j] = POSITIONSTATE.FREE;
            }
        }
    }

    public Board(POSITIONSTATE[][] boardPositionsStates){
        if (boardPositionsStates.length != 3 || boardPositionsStates[0].length != 8){
            throw  new IllegalArgumentException("Arraydimensionen des übergebenen Boards stimmen nicht.");
        } else {
            this.boardPositionsStates = boardPositionsStates;
        }
    }




    public UUID getBoardUuid() {
        return boardUuid;
    }


    public POSITIONSTATE[][] getBoardPositionsStates() {
        return boardPositionsStates;
    }
}
