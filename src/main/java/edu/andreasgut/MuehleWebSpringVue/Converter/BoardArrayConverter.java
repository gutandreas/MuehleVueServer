package edu.andreasgut.MuehleWebSpringVue.Converter;

import edu.andreasgut.MuehleWebSpringVue.Models.POSITIONSTATE;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BoardArrayConverter implements AttributeConverter<POSITIONSTATE[][], String> {

    @Override
    public String convertToDatabaseColumn(POSITIONSTATE[][] boardPositionsStates) {

        StringBuilder boardAsString = new StringBuilder();

        for (int i = 0; i < boardPositionsStates.length; i++) {
            for (int j = 0; j < boardPositionsStates[0].length; j++) {
                switch (boardPositionsStates[i][j]) {
                    case FREE -> boardAsString.append("0");
                    case PLAYER1 -> boardAsString.append("1");
                    case PLAYER2 -> boardAsString.append("2");

                }

            }

        }

        return boardAsString.toString();
    }

    @Override
    public POSITIONSTATE[][] convertToEntityAttribute(String boardAsString) {

        POSITIONSTATE[][]  boardPositionsStates = new POSITIONSTATE[3][8];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                char tempChar = boardAsString.charAt(i * 8 + j);

                switch (tempChar) {
                    case '0' -> boardPositionsStates[i][j] = POSITIONSTATE.FREE;
                    case '1' -> boardPositionsStates[i][j] = POSITIONSTATE.PLAYER1;
                    case '2' -> boardPositionsStates[i][j] = POSITIONSTATE.PLAYER2;
                }

            }
        }
        return boardPositionsStates;
    }
}