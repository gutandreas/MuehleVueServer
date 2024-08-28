package edu.andreasgut.MuehleWebSpringVue.Models;


import edu.andreasgut.MuehleWebSpringVue.Converter.BoardArrayConverter;
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






    public POSITIONSTATE[][] getBoardPositionsStates() {
        return boardPositionsStates;
    }


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





    public POSITIONSTATE getStateOfPosition(Position position){
        return boardPositionsStates[position.getRing()][position.getField()];
    }



    public int getNumberOfStates(POSITIONSTATE positionstate){
        int counter = 0;
        for (int i = 0; i < boardPositionsStates.length; i++) {
            for (int j = 0; j < boardPositionsStates[0].length; j++) {
                if (boardPositionsStates[i][j] == positionstate){
                    counter++;
                }
            }
        }
        return counter;

    }

    public int getNumberOfStonesOfPlayerWithIndex(int index){

        POSITIONSTATE state = index == 1 ? POSITIONSTATE.PLAYER1 : POSITIONSTATE.PLAYER2;
        return getNumberOfStates(state);

    }

    public void putStone(Position position, int playerIndex) {
        setStoneOnPositionInArray(position, playerIndex);
    }

    private boolean isPutPossibleAt(Position position){
        return boardPositionsStates[position.getRing()][position.getField()] == POSITIONSTATE.FREE;
    }

    public void moveStone(Move move, int playerIndex) {
        setStoneOnPositionInArray(move.getTo(), playerIndex);
        removeStoneFromPositionInArray(move.getFrom());
    }

    public void killStone(Position position) {
        removeStoneFromPositionInArray(position);
    }

    private void removeStoneFromPositionInArray(Position position){
        boardPositionsStates[position.getRing()][position.getField()] = POSITIONSTATE.FREE;
    }

    private void setStoneOnPositionInArray(Position position, int playerIndex){
        POSITIONSTATE positionstate = playerIndex == 1 ? POSITIONSTATE.PLAYER1 : POSITIONSTATE.PLAYER2;
        boardPositionsStates[position.getRing()][position.getField()] = positionstate;

    }

    public boolean isPositionPartOfMorris(Position position) {
        boolean cornerField = position.getField() % 2 == 0;
        boolean morris;
        POSITIONSTATE positionstate = boardPositionsStates[position.getRing()][position.getField()];

        if (cornerField) {
            morris = isPositionPartOfMorrisInRingFromCornerField(position, positionstate);
        } else {
            morris = isPositionPartOfMorrisInRingFromCenterField(position, positionstate) || isPositionPartOfMorrisBetweenRings(position, positionstate);
        }

        return morris;
    }

    private boolean isPositionPartOfMorrisInRingFromCornerField(Position position, POSITIONSTATE positionstate) {

        boolean morrisUpwards = positionstate == boardPositionsStates[position.getRing()][(position.getField() + 1) % 8]
                && positionstate == boardPositionsStates[position.getRing()][(position.getField() + 2) % 8];
        boolean morrisDownwards = positionstate == boardPositionsStates[position.getRing()][(position.getField() + 6) % 8]
                && positionstate == boardPositionsStates[position.getRing()][(position.getField() + 7) % 8];

        return morrisUpwards || morrisDownwards;
    }


    private boolean isPositionPartOfMorrisInRingFromCenterField(Position position, POSITIONSTATE positionstate) {
        return positionstate == boardPositionsStates[position.getRing()][(position.getField() + 1) % 8]
                && positionstate == boardPositionsStates[position.getRing()][(position.getField() + 7) % 8];
    }


    private boolean isPositionPartOfMorrisBetweenRings(Position position, POSITIONSTATE positionstate) {
        return positionstate == boardPositionsStates[(position.getRing() + 1) % 3][position.getField()]
                && positionstate == boardPositionsStates[(position.getRing() + 2) % 3][position.getField()];
    }







}
