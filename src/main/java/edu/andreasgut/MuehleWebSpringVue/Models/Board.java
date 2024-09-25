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
public class Board implements Cloneable {

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

    @Override
    public Board clone() {
        try {
            Board cloned = (Board) super.clone();

            // Tiefes Kopieren des 2D-Arrays für die Positionen
            cloned.boardPositionsStates = new POSITIONSTATE[boardPositionsStates.length][boardPositionsStates[0].length];
            for (int i = 0; i < boardPositionsStates.length; i++) {
                System.arraycopy(boardPositionsStates[i], 0, cloned.boardPositionsStates[i], 0, boardPositionsStates[i].length);
            }

            return cloned;

        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Spielersteine als Zeichen zuordnen (1 für Spieler 1, 2 für Spieler 2)
        String[][] display = new String[3][8];
        for (int i = 0; i < boardPositionsStates.length; i++) {
            for (int j = 0; j < boardPositionsStates[i].length; j++) {
                switch (boardPositionsStates[i][j]) {
                    case PLAYER1:
                        display[i][j] = "1";
                        break;
                    case PLAYER2:
                        display[i][j] = "2";
                        break;
                    case FREE:
                    default:
                        display[i][j] = "0";
                        break;
                }
            }
        }

        // Zeichne das Spielfeld
        sb.append(String.format(
                        "%s---------%s---------%s%n" +
                        "|         |         |%n" +
                        "|  %s------%s------%s  |%n" +
                        "|  |      |      |  |%n" +
                        "|  |  %s---%s---%s  |  |%n" +
                        "|  |  |       |  |  |%n" +
                        "%s--%s--%s       %s--%s--%s%n" +
                        "|  |  |       |  |  |%n" +
                        "|  |  %s---%s---%s  |  |%n" +
                        "|  |      |      |  |%n" +
                        "|  %s------%s------%s  |%n" +
                        "|         |         |%n" +
                        "%s---------%s---------%s%n",
                display[0][0], display[0][1], display[0][2],  // Äußerer Ring
                display[1][0], display[1][1], display[1][2],  // Mittlerer Ring
                display[2][0], display[2][1], display[2][2],  // Innerer Ring
                display[0][7], display[1][7], display[2][7], display[2][3], display[1][3], display[0][3],
                display[2][6], display[2][5], display[2][4],  // Mitte links/rechts
                display[1][6], display[1][5], display[1][4],  // Mittlerer Ring links/rechts
                display[0][6], display[0][5], display[0][4]   // Äußerer Ring links/rechts
        ));

        return sb.toString();
    }
}
