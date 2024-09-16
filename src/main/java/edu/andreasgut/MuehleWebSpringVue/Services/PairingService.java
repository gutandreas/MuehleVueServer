package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Pairing;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PairingService {

    private static final Logger logger = LoggerFactory.getLogger(PairingService.class);
    public void addSecondPlayer(Pairing pairing, Player player2) {

        if (player2 == null){
            throw new IllegalArgumentException("Ein übergebener Player ist null");
        }
        if (getPlayer2(pairing) != null){
            throw new IllegalArgumentException("Der zweite Spieler ist bereits belegt");
        }

        pairing.setPlayer2(player2);

        pairing.setComplete(true);
    }

    public Player getPlayerByIndex(Pairing pairing, int index){
        return index == 1 ? pairing.getPlayer1() : pairing.getPlayer2();
    }



    public int getCurrentPlayerIndex(Pairing pairing) {
        return pairing.getCurrentPlayerIndex();
    }

    public Player getCurrentPlayer(Pairing pairing) {
        return pairing.getCurrentPlayerIndex() == 1 ? pairing.getPlayer1() : pairing.getPlayer2();
    }

    public Player getEnemyOf(Pairing pairing, Player player){
        return player.equals(pairing.getPlayer1()) ? pairing.getPlayer2() : pairing.getPlayer1();
    }

    public void changeTurn(Pairing pairing) {
        pairing.setCurrentPlayerIndex( pairing.getCurrentPlayerIndex() == 1 ? 2 : 1);
    }


    public Player getPlayerByPlayerUuid(Pairing pairing, String playerUuid){
        String player1Uuid = pairing.getPlayer1().getUuid();
        String player2Uuid = pairing.getPlayer2().getUuid();

        if (!player1Uuid.equals(playerUuid) && !player2Uuid.equals(playerUuid)){
            logger.warn("Ungültige playerUuid: " + player1Uuid);
            throw new IllegalArgumentException(Class.class.getSimpleName() + "-  Ungültige playerUuid");
        }

        if (player1Uuid.equals(playerUuid)){
            return pairing.getPlayer1();
        } else {
            return pairing.getPlayer2();
        }



    }

    public int getPlayerIndexByPlayerUuid(Pairing pairing, String playerUuid){
        String player1Uuid = pairing.getPlayer1().getUuid();
        String player2Uuid = pairing.getPlayer2().getUuid();

        if (!player1Uuid.equals(playerUuid) && !player2Uuid.equals(playerUuid)){
            throw new IllegalArgumentException(Class.class.getSimpleName() + "-  Ungültige playerUuid");
        }

        if (player1Uuid.equals(playerUuid)){
            return 1;
        } else {
            return 2;
        }
    }

    public Player getPlayer1(Pairing pairing) {
        return pairing.getPlayer1();
    }

    public Player getPlayer2(Pairing pairing) {
        return pairing.getPlayer2();
    }

    public int getIndexOfPlayer(Pairing pairing, Player player){
        return getPlayer1(pairing).equals(player) ? 1 : 2;
    }

    public boolean isComplete(Pairing pairing) {
        return pairing.isComplete();
    }
}
