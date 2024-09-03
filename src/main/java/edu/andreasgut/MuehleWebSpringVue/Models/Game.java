package edu.andreasgut.MuehleWebSpringVue.Models;


import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.ParticipantGroup;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Entity
public class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);


    @Id
    private String gameCode;
    private int round;
    private boolean finished;
    private int winnerIndex;
    @Transient
    private ParticipantGroup participantGroup;




    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_fk")
    private Board board;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pairing_fk")
    private Pairing pairing;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "spectators_fk")
    private List<Spectator> spectators = new LinkedList<>();

    public void setBoard(Board board) {
        this.board = board;
    }

    public ParticipantGroup getGroup() {
        return participantGroup;
    }

    public void setGroup(ParticipantGroup participantGroup) {
        this.participantGroup = participantGroup;
    }


    public Game(String gameCode, Board board, Pairing pairing, int round) {
        this.gameCode = gameCode;
        this.board = board;
        this.pairing = pairing;
        this.round = round;
        this.finished = false;
    }

    public Game() {
        // Leerer Konstruktor für Spring Repository
    }


    public void increaseRound(){
        round++;
    }


    public boolean addSpectator(Spectator spectator){
        boolean alreadyExists = spectators.stream()
                .anyMatch(s -> s.getWebSocketSessionId().equals(spectator.getWebSocketSessionId()));

        if (!alreadyExists) {
            spectators.add(spectator);
            return true;
        }
        return false;
    }

    public boolean executePut(Put put, String uuid){
        boolean phaseOk = pairing.getPlayerByPlayerUuid(uuid).getCurrentPhase() == PHASE.PUT;
        boolean positionOk = board.isPositionFree(put.getPutPosition());
        if (phaseOk && positionOk){
            int index = getPairing().getPlayerIndexByPlayerUuid(uuid);
            board.putStone(put.getPutPosition(), index);
            Player activePlayer = pairing.getPlayerByIndex(index);
            activePlayer.increaseNumberOfStonesPut();
            updateRoundAndPhasesOfPlayersAfterPut(put);
            logger.info("Put in Game ausgeführt");
            return true;
        } else {
            logger.warn("Put ungültig");
            return false;
        }
    }

    public boolean executeKill(Kill kill, String uuid){
        boolean phaseOk = pairing.getPlayerByPlayerUuid(uuid).getCurrentPhase() == PHASE.KILL;
        Player activePlayer = pairing.getPlayerByPlayerUuid(uuid);
        Player passivePlayer = pairing.getEnemyOf(activePlayer);
        int passivePlayerIndex = pairing.getIndexOfPlayer(passivePlayer);
        boolean positionOk = board.isThisPositionOccupiedByPlayerWithIndex(passivePlayerIndex, kill.getKillPosition());

        if (phaseOk && positionOk){
            board.killStone(kill.getKillPosition());
            activePlayer.increaseNumberOfStonesKilled();
            passivePlayer.increaseNumberOfStonesLost();
            updateRoundAndPhasesOfPlayersAfterKill();
            if (isGameWon()){
                calculateWinnerAndFinishGame();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean executeMove(Move move, String uuid){
        boolean phaseOk = pairing.getPlayerByPlayerUuid(uuid).getCurrentPhase() == PHASE.MOVE;
        int playerIndex = pairing.getPlayerIndexByPlayerUuid(uuid);
        boolean fromPositionOk = board.isThisPositionOccupiedByPlayerWithIndex(playerIndex, move.getFrom());
        boolean toPositionOk = board.isPositionFree(move.getTo());
        boolean positionsAreNeighbours = board.arePositionsNeighbours(move.getFrom(), move.getTo());

        if (phaseOk && fromPositionOk && toPositionOk && positionsAreNeighbours){
            board.moveStone(move, playerIndex);
            updateRoundAndPhasesOfPlayersAfterMove(move);
            return true;
        } else {
            return false;
        }

    }

    public boolean executeJump(Jump jump, String uuid){

        boolean phaseOk = pairing.getPlayerByPlayerUuid(uuid).getCurrentPhase() == PHASE.JUMP;
        int playerIndex = pairing.getPlayerIndexByPlayerUuid(uuid);
        boolean fromPositionOk = board.isThisPositionOccupiedByPlayerWithIndex(playerIndex, jump.getFrom());
        boolean toPositionOk = board.isPositionFree(jump.getTo());

        if (phaseOk && fromPositionOk && toPositionOk){
            board.jumpStone(jump, playerIndex);
            updateRoundAndPhasesOfPlayersAfterJump(jump);
            return true;
        } else {
            return false;
        }

    }

    private void updateRoundAndPhasesOfPlayersAfterPut(Put put){

        boolean buildsMorris = board.isPositionPartOfMorris(put.getPutPosition());

        if (buildsMorris){
            pairing.getCurrentPlayer().setCurrentPhase(PHASE.KILL);
            return;
        } else {
            increaseRound();
            pairing.changeTurn();
            Player activePlayer = pairing.getCurrentPlayer();
            Player passivePlayer = pairing.getEnemyOf(activePlayer);
            passivePlayer.setCurrentPhase(PHASE.WAIT);
            if (round < 18) {
                activePlayer.setCurrentPhase(PHASE.PUT);
            } else {
                int index = pairing.getIndexOfPlayer(activePlayer);
                if (board.getNumberOfStonesOfPlayerWithIndex(index) > 3) {
                    activePlayer.setCurrentPhase(PHASE.MOVE);
                } else {
                    activePlayer.setCurrentPhase(PHASE.JUMP);
                }
            }
        }

    }

    private void updateRoundAndPhasesOfPlayersAfterMove(Move move){

        boolean buildsMorris = board.isPositionPartOfMorris(move.getTo());

        if (buildsMorris){
            pairing.getCurrentPlayer().setCurrentPhase(PHASE.KILL);
            return;
        } else {
            increaseRound();
            pairing.changeTurn();
            Player activePlayer = pairing.getCurrentPlayer();
            Player passivePlayer = pairing.getEnemyOf(activePlayer);
            passivePlayer.setCurrentPhase(PHASE.WAIT);
            if (round < 18) {
                activePlayer.setCurrentPhase(PHASE.PUT);
            } else {
                int index = pairing.getIndexOfPlayer(activePlayer);
                if (board.getNumberOfStonesOfPlayerWithIndex(index) > 3) {
                    activePlayer.setCurrentPhase(PHASE.MOVE);
                } else {
                    activePlayer.setCurrentPhase(PHASE.JUMP);
                }
            }
        }

    }

    private boolean isGameWon(){
        return round > 18 && (board.getNumberOfStonesOfPlayerWithIndex(1) < 3 || board.getNumberOfStonesOfPlayerWithIndex(2) < 3);
    }

    private void calculateWinnerAndFinishGame(){
        finished = true;
        winnerIndex = board.getNumberOfStonesOfPlayerWithIndex(1) == 2 ? 2 : 1;
    }

    private void updateRoundAndPhasesOfPlayersAfterKill(){
        increaseRound();
        pairing.changeTurn();
        Player activePlayer = pairing.getCurrentPlayer();
        Player passivePlayer = pairing.getEnemyOf(activePlayer);
        passivePlayer.setCurrentPhase(PHASE.WAIT);
        if (round < 18) {
            activePlayer.setCurrentPhase(PHASE.PUT);
        } else {
            int index = pairing.getIndexOfPlayer(activePlayer);
            if (board.getNumberOfStonesOfPlayerWithIndex(index) > 3) {
                activePlayer.setCurrentPhase(PHASE.MOVE);
            } else {
                activePlayer.setCurrentPhase(PHASE.JUMP);
            }
        }
    }

    private void updateRoundAndPhasesOfPlayersAfterJump(Jump jump){

        boolean buildsMorris = board.isPositionPartOfMorris(jump.getTo());

        if (buildsMorris){
            pairing.getCurrentPlayer().setCurrentPhase(PHASE.KILL);
            return;
        } else {
            increaseRound();
            pairing.changeTurn();
            Player activePlayer = pairing.getCurrentPlayer();
            Player passivePlayer = pairing.getEnemyOf(activePlayer);
            passivePlayer.setCurrentPhase(PHASE.WAIT);
            if (round < 18) {
                activePlayer.setCurrentPhase(PHASE.PUT);
            } else {
                int index = pairing.getIndexOfPlayer(activePlayer);
                if (board.getNumberOfStonesOfPlayerWithIndex(index) > 3) {
                    activePlayer.setCurrentPhase(PHASE.MOVE);
                } else {
                    activePlayer.setCurrentPhase(PHASE.JUMP);
                }
            }
        }

    }








    // Setter und Getter

    public String getGameCode() {
        return gameCode;
    }

    public Board getBoard() {
        return board;
    }

    public Pairing getPairing() {
        return pairing;
    }


    public List<Spectator> getSpectators() {
        return spectators;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getRound() {
        return round;
    }

    public ParticipantGroup getParticipantGroup() {
        return participantGroup;
    }
}

