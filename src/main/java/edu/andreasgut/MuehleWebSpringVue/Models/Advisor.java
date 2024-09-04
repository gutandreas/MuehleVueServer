package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;

import java.util.LinkedList;

public class Advisor {

    public static LinkedList<Put> getPossiblePuts(Game game, String uuid) {
        LinkedList<Put> possiblePuts = new LinkedList<>();
        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                if (game.getBoard().isPositionFree(new Position(ring, field))) {
                    possiblePuts.add(new Put(uuid, new Position(ring, field)));
                }
            }
        }
        return possiblePuts;
    }

    public static LinkedList<Kill> getPossibleKills(Game game, String uuid) {
        LinkedList<Kill> possibleKills = new LinkedList<>();
        Pairing pairing = game.getPairing();
        int enemyIndex = pairing.getPlayerIndexByPlayerUuid(uuid) == 1 ? 2 : 1;
        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                if (game.getBoard().isThisPositionOccupiedByPlayerWithIndex(enemyIndex, new Position(ring, field))) {
                    possibleKills.add(new Kill(uuid, new Position(ring, field)));
                }
            }
        }
        return possibleKills;
    }

    public static LinkedList<Move> getPossibleMoves(Game game, String uuid) {
        LinkedList<Move> possibleMoves = new LinkedList<>();
        int index = game.getPairing().getPlayerIndexByPlayerUuid(uuid);
        Board board = game.getBoard();

        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {

                Position from = new Position(ring, field);
                if (board.isThisPositionOccupiedByPlayerWithIndex(index, from)) {
                    if (board.isPositionFree(new Position(ring, (field + 1) % 8))) {
                        Move move = new Move(uuid, from, new Position(ring, (field + 1) % 8));
                        possibleMoves.add(move);
                    }
                    if (board.isPositionFree(new Position(ring, (field + 7) % 8))) {
                        Move move = new Move(uuid, from, new Position(ring, (field + 7) % 8));
                        possibleMoves.add(move);
                    }
                    if (field % 2 == 1 && (ring == 0 || ring == 1) && board.isPositionFree(new Position(ring + 1, field))) {
                        Move move = new Move(uuid, from, new Position(ring + 1, field));
                        possibleMoves.add(move);
                    }
                    if (field % 2 == 1 && (ring == 1 || ring == 2) && board.isPositionFree(new Position(ring - 1, field))) {
                        Move move = new Move(uuid, from, new Position(ring - 1, field));
                        possibleMoves.add(move);
                    }
                }
            }
        }
        return possibleMoves;
    }

    public static LinkedList<Jump> getPossibleJumps(Game game, String uuid) {
        LinkedList<Jump> possibleJumps = new LinkedList<>();
        int index = game.getPairing().getPlayerIndexByPlayerUuid(uuid);
        Board board = game.getBoard();

        for (int ringFrom = 0; ringFrom < 3; ringFrom++) {
            for (int fieldFrom = 0; fieldFrom < 8; fieldFrom++) {

                Position from = new Position(ringFrom, fieldFrom);
                if (board.isThisPositionOccupiedByPlayerWithIndex(index, from)) {
                    for (int ringTo = 0; ringTo < 3; ringTo++) {
                        for (int fieldTo = 0; fieldTo < 8; fieldTo++) {
                            Jump jump = new Jump(uuid, from, new Position(ringTo, fieldTo));
                            possibleJumps.add(jump);
                        }

                    }
                }
            }
        }

        return possibleJumps;
    }
}
