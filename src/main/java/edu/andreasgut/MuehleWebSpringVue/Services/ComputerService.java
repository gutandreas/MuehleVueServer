package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.AlphaBeta.GameNode;
import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Random;

@Service
public class ComputerService {

    private static final Logger logger = LoggerFactory.getLogger(ComputerService.class);
    BoardService boardService;
    GameService gameService;
    PlayerService playerService;
    GameStateService gameStateService;
    PairingService pairingService;



    public ComputerService(BoardService boardService, GameService gameService, PlayerService playerService, GameStateService gameStateService, PairingService pairingService) {
        this.boardService = boardService;
        this.gameService = gameService;
        this.playerService = playerService;
        this.gameStateService = gameStateService;
        this.pairingService = pairingService;
    }

    public void triggerComputer() {

    }



    public Put calculatePut(Game game, int playerIndex){

        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) game.getPairing().getPlayerByIndex(playerIndex);
        int level = standardComputerPlayer.getLevel();


        switch (level){
            case 0:
                return getRandomPut(game);
            case 1:
                return (Put) executeAlphaBeta(game, playerIndex, 3);
            default:
                return null;
        }


    }

    private Put getRandomPut(Game game){
        logger.info("Neuer zufälliger Put wird berechnet");
        LinkedList<Put> possiblePuts = boardService.getPossiblePuts(game.getBoard());
        return possiblePuts.get(new Random().nextInt(possiblePuts.size()));
    }



    private GameAction executeAlphaBeta(Game game, int ownIndex, int maxLevel){

        Game clonedGame = game.clone();

        int currentLevel = 0;

        GameNode root = new GameNode(clonedGame.getBoard(), null, ownIndex, null, 0);
        PHASE currentPhase = playerService.getPhase(pairingService.getCurrentPlayer(game.getPairing()));

        recursiveAlphaBeta(clonedGame, ownIndex, maxLevel, currentLevel, currentPhase, root);

        root.printTree();

        return new Put(new Position(2, 2));
    }

    private void recursiveAlphaBeta(Game game, int ownIndex, int maxLevel, int currentLevel, PHASE currentPhase, GameNode parent){
        if (currentLevel == maxLevel){
            return;
        }




        switch (currentPhase){
            case PUT:
                LinkedList<Put> possiblePuts = boardService.getPossiblePuts(game.getBoard());
                for (Put put : possiblePuts){
                    Game clonedGame = game.clone();
                    boolean maximize = clonedGame.getPairing().getCurrentPlayerIndex() == ownIndex;
                    Player currentPlayer = pairingService.getCurrentPlayer(clonedGame.getPairing());
                    Player enemyPlayer = pairingService.getEnemyOf(clonedGame.getPairing(), currentPlayer);
                    int currentPlayerIndex = pairingService.getCurrentPlayerIndex(clonedGame.getPairing());
                    boardService.putStone(clonedGame.getBoard(), put, currentPlayerIndex);
                    System.out.println(clonedGame.getBoard());
                    GameNode gameNode = new GameNode(clonedGame.getBoard(), put, currentPlayerIndex, parent, evaluateScore(clonedGame.getBoard(), currentPlayerIndex));
                    playerService.increasePutStones(currentPlayer);
                    if (!boardService.isPositionPartOfMorris(clonedGame.getBoard(), put.getPutPosition())){
                        gameStateService.increaseRound(clonedGame.getGameState());
                        pairingService.changeTurn(clonedGame.getPairing());
                        playerService.changeToWaitPhase(currentPlayer);
                        playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                    } else {
                        playerService.changeToKillPhase(currentPlayer);
                    }
                    PHASE nextPhase = playerService.getPhase(pairingService.getCurrentPlayer(clonedGame.getPairing()));
                    recursiveAlphaBeta(clonedGame, ownIndex, maxLevel,  currentLevel + 1, nextPhase, gameNode);
                }
                break;
            case MOVE:
                LinkedList<Move> possibleMoves = boardService.getPossibleMoves(game.getBoard(), pairingService.getCurrentPlayerIndex(game.getPairing()));
                for (Move move : possibleMoves){
                    Game clonedGame = game.clone();
                    boolean maximize = clonedGame.getPairing().getCurrentPlayerIndex() == ownIndex;
                    Player currentPlayer = pairingService.getCurrentPlayer(clonedGame.getPairing());
                    Player enemyPlayer = pairingService.getEnemyOf(clonedGame.getPairing(), currentPlayer);
                    int currentPlayerIndex = pairingService.getCurrentPlayerIndex(clonedGame.getPairing());
                    boardService.moveStone(clonedGame.getBoard(), move, currentPlayerIndex);
                    System.out.println(clonedGame.getBoard());
                    GameNode gameNode = new GameNode(clonedGame.getBoard(), move, currentPlayerIndex, parent, evaluateScore(clonedGame.getBoard(), currentPlayerIndex));
                    playerService.increasePutStones(currentPlayer);
                    if (!boardService.isPositionPartOfMorris(clonedGame.getBoard(), move.getTo())){
                        gameStateService.increaseRound(clonedGame.getGameState());
                        pairingService.changeTurn(clonedGame.getPairing());
                        playerService.changeToWaitPhase(currentPlayer);
                        playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                    } else {
                        playerService.changeToKillPhase(currentPlayer);
                    }
                    PHASE nextPhase = playerService.getPhase(pairingService.getCurrentPlayer(clonedGame.getPairing()));
                    recursiveAlphaBeta(clonedGame, ownIndex, maxLevel,  currentLevel + 1, nextPhase, gameNode);
                }
                break;
            case KILL:
                LinkedList<Kill> possibleKills = boardService.getPossibleKills(game.getBoard(), pairingService.getCurrentPlayerIndex(game.getPairing()));
                for (Kill kill : possibleKills){
                    Game clonedGame = game.clone();
                    boolean maximize = clonedGame.getPairing().getCurrentPlayerIndex() == ownIndex;
                    Player currentPlayer = pairingService.getCurrentPlayer(clonedGame.getPairing());
                    Player enemyPlayer = pairingService.getEnemyOf(clonedGame.getPairing(), currentPlayer);
                    int currentPlayerIndex = pairingService.getCurrentPlayerIndex(clonedGame.getPairing());
                    boardService.killStone(clonedGame.getBoard(), kill);
                    System.out.println(clonedGame.getBoard());
                    GameNode gameNode = new GameNode(clonedGame.getBoard(), kill, currentPlayerIndex, parent, evaluateScore(clonedGame.getBoard(), currentPlayerIndex));
                    playerService.increasePutStones(currentPlayer);
                    if (!boardService.isPositionPartOfMorris(clonedGame.getBoard(), kill.getKillPosition())){
                        gameStateService.increaseRound(clonedGame.getGameState());
                        pairingService.changeTurn(clonedGame.getPairing());
                        playerService.changeToWaitPhase(currentPlayer);
                        playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                    } else {
                        playerService.changeToKillPhase(currentPlayer);
                    }
                    PHASE nextPhase = playerService.getPhase(pairingService.getCurrentPlayer(clonedGame.getPairing()));
                    recursiveAlphaBeta(clonedGame, ownIndex, maxLevel,  currentLevel + 1, nextPhase, gameNode);
                }
                break;
        }




    }




    private int evaluateScore(Board board, int playerIndex){
        int stonesWeight = 100;
        int movesWeight = 1;
        int ownStones = boardService.getNumberOfStonesOfPlayerWithIndex(board, playerIndex);
        int ownMoves = boardService.getPossibleMoves(board, playerIndex).size();
        int enemyIndex = playerIndex == 1 ? 2 : 1;
        int enemyStones = boardService.getNumberOfStonesOfPlayerWithIndex(board, enemyIndex);
        int enemyMoves = boardService.getPossibleMoves(board, enemyIndex).size();
        int score = stonesWeight * (ownStones - enemyStones) + movesWeight * (ownMoves - enemyMoves);

        return score;

    }

    public Move calculateMove(StandardComputerPlayer standardComputerPlayer, Board board, int playerIndex){
        logger.info("Neuer Move wird berechnet");
        LinkedList<Move> possibleMoves = boardService.getPossibleMoves(board, playerIndex);
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

    public Jump calculateJump(StandardComputerPlayer standardComputerPlayer, Board board, int playerIndex){
        logger.info("Neuer Jump wird berechnet");
        LinkedList<Jump> possibleJumps = boardService.getPossibleJumps(board, playerIndex);
        return possibleJumps.get(new Random().nextInt(possibleJumps.size()));
    }

    public Kill calculateKill(StandardComputerPlayer standardComputerPlayer, Board board, int playerIndex){
        logger.info("Neuer Kill wird berechnet");
        LinkedList<Kill> possibleKills = boardService.getPossibleKills(board, playerIndex);
        return possibleKills.get(new Random().nextInt(possibleKills.size()));
    }



}
