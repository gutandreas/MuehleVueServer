package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Converter.BoardArrayConverter;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class BoardPersistent extends Board  {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID boardUuid;



    public BoardPersistent() {
        super(); // Ruft den Konstruktor der Basisklasse auf
    }

    public BoardPersistent(POSITIONSTATE[][] boardPositionsStates){
        super(boardPositionsStates); // Ruft den Konstruktor der Basisklasse auf
    }

    public UUID getBoardUuid() {
        return boardUuid;
    }

    @Convert(converter = BoardArrayConverter.class)
    @Override
    public POSITIONSTATE[][] getBoardPositionsStates() {
        return super.getBoardPositionsStates();
    }





}
