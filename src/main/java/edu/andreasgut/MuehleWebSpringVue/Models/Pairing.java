package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Services.GameActionService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Entity
public class Pairing {

    private static final Logger logger = LoggerFactory.getLogger(Pairing.class);


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID pairingUuid;

    @OneToOne(cascade = CascadeType.ALL)
    private Player player1;

    @OneToOne(cascade = CascadeType.ALL)
    private Player player2;

    private int currentPlayerIndex;

    private int startPlayerIndex;

    private boolean complete = false;

    public Pairing(Player player1, Player player2, int startPlayerIndex) {

        if (startPlayerIndex != 1 && startPlayerIndex != 2){
            throw new IllegalArgumentException("Ungültiger Playerindex");
        }

        if (player1 == null || player2 == null){
            throw new IllegalArgumentException("Ein übergebener Player ist null");
        }

        this.player1 = player1;
        this.player2 = player2;
        this.startPlayerIndex = startPlayerIndex;

        complete = true;
        currentPlayerIndex = startPlayerIndex;
    }

    public Pairing(Player player1, int startPlayerIndex) {

        if (startPlayerIndex != 1 && startPlayerIndex != 2){
            throw new IllegalArgumentException("Ungültiger Playerindex");
        }

        if (player1 == null){
            throw new IllegalArgumentException("Ein übergebener Player ist null");
        }

        this.player1 = player1;
        this.startPlayerIndex = startPlayerIndex;
    }

    public Pairing() {
    }

    public void addSecondPlayer(Player player2) {

        if (player2 == null){
            throw new IllegalArgumentException("Ein übergebener Player ist null");
        }
        if (this.player2 != null){
            throw new IllegalArgumentException("Der zweite Spieler ist bereits belegt");
        }

        this.player2 = player2;

        complete = true;
        currentPlayerIndex = startPlayerIndex;
    }

    public Player getPlayerByIndex(int index){
        return index == 1 ? player1 : player2;
    }



    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return currentPlayerIndex == 1 ? player1 : player2;
    }

    public Player getEnemyOf(Player player){
        return player.equals(player1) ? player2 : player1;
    }

    public void changeTurn() {
        currentPlayerIndex = currentPlayerIndex == 1 ? 2 :1;
    }


    public Player getPlayerByPlayerUuid(String playerUuid){
        String player1Uuid = player1.getUuid();
        String player2Uuid = player2.getUuid();

        if (!player1Uuid.equals(playerUuid) && !player2Uuid.equals(playerUuid)){
            logger.warn("Ungültige playerUuid: " + player1Uuid);
            throw new IllegalArgumentException(Class.class.getSimpleName() + "-  Ungültige playerUuid");
        }

        if (player1Uuid.equals(playerUuid)){
            return player1;
        } else {
            return player2;
        }



    }

    public int getPlayerIndexByPlayerUuid(String playerUuid){
        String player1Uuid = player1.getUuid();
        String player2Uuid = player2.getUuid();

        if (!player1Uuid.equals(playerUuid) && !player2Uuid.equals(playerUuid)){
            throw new IllegalArgumentException(Class.class.getSimpleName() + "-  Ungültige playerUuid");
        }

        if (player1Uuid.equals(playerUuid)){
            return 1;
        } else {
            return 2;
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getIndexOfPlayer(Player player){
        return player1.equals(player) ? 1 : 2;
    }

    public boolean isComplete() {
        return complete;
    }
}
