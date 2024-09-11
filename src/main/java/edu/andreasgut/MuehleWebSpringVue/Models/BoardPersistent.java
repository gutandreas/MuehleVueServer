package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Converter.BoardArrayConverter;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class BoardPersistent extends Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID boardUuid;

    @Convert(converter = BoardArrayConverter.class)
    POSITIONSTATE[][] boardPositionsStates;


    public POSITIONSTATE[][] getBoardPositionsStates() {
        return boardPositionsStates;
    }


    public BoardPersistent() {
        super();
    }

    public BoardPersistent(POSITIONSTATE[][] boardPositionsStates){
        super(boardPositionsStates);
    }
}
