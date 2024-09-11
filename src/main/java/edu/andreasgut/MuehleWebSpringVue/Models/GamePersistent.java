package edu.andreasgut.MuehleWebSpringVue.Models;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
public class GamePersistent extends Game{

    @Id
    String gameCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_fk")
    private BoardPersistent board;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pairing_fk")
    private PairingPersistent pairing;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "spectators_fk")
    private List<Spectator> spectators = new LinkedList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gamestate_fk")
    private GameStatePersistent gameState;





    public GamePersistent(String gameCode, GameStatePersistent gameState, PairingPersistent pairing, BoardPersistent board) {
        super(gameState, pairing, board);
        System.out.println(gameState);
        System.out.println(pairing);
        System.out.println(board);
        this.gameCode = gameCode;
    }

    public GamePersistent() {
    }

    public GameStatePersistent getGameState() {
        return gameState;
    }

    public PairingPersistent getPairing() {
        return pairing;
    }

    public BoardPersistent getBoard() {
        return board;
    }

    public List<Spectator> getSpectators() {
        return spectators;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public void setBoard(BoardPersistent board) {
        this.board = board;
    }

    public void setPairing(PairingPersistent pairing) {
        this.pairing = pairing;
    }

    public void setSpectators(List<Spectator> spectators) {
        this.spectators = spectators;
    }

    public void setGameState(GameStatePersistent gameState) {
        this.gameState = gameState;
    }
}
