package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;

import java.util.LinkedList;

public class Advisor {

    public static LinkedList<Put> getPossiblePuts(Game game, String uuid){
        LinkedList<Put> possiblePuts = new LinkedList<>();
        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                if (game.getBoard().isPositionFree(new Position(ring, field))){
                    possiblePuts.add(new Put(uuid, new Position(ring, field)));
                }
            }
        }
        return possiblePuts;
    }

    public static LinkedList<Kill> getPossibleKills(Game game, String uuid){
        LinkedList<Kill> possibleKills = new LinkedList<>();
        Pairing pairing = game.getPairing();
        int enemyIndex = pairing.getPlayerIndexByPlayerUuid(uuid) == 1 ? 2 : 1;
        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                if (game.getBoard().isThisPositionOccupiedByPlayerWithIndex(enemyIndex, new Position(ring, field))){
                    possibleKills.add(new Kill(uuid, new Position(ring, field)));
                }
            }
        }
        return possibleKills;
    }

}
