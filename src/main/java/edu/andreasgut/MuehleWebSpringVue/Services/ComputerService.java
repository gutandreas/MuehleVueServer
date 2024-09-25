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
                return getRandomPut(game, playerIndex);
            case 1:
                return (Put) executeAlphaBeta(game, playerIndex, 2);
            default:
                return null;
        }


    }

    private Put getRandomPut(Game game, int playerIndex){
        logger.info("Neuer zufälliger Put wird berechnet");
        LinkedList<Put> possiblePuts = boardService.getPossiblePuts(game.getBoard(), playerIndex);
        return possiblePuts.get(new Random().nextInt(possiblePuts.size()));
    }



    private GameAction executeAlphaBeta(Game game, int ownIndex, int maxLevel){

        Game clonedGame = game.clone();

        int currentLevel = 0;

        GameNode root = new GameNode(clonedGame.getBoard(), null, ownIndex, null, 0);

        recursiveAlphaBeta(clonedGame, ownIndex, maxLevel, currentLevel, root);

        root.printTree();

        return new Put(new Position(2, 2));
    }

    private void recursiveAlphaBeta(Game game, int ownIndex, int maxLevel, int currentLevel, GameNode parent){
        if (currentLevel == maxLevel){
            return;
        }


        boolean maximize = game.getPairing().getCurrentPlayerIndex() == ownIndex;
        PHASE currentPhase = game.getPairing().getCurrentPlayer().getCurrentPhase();
        Player currentPlayer = pairingService.getCurrentPlayer(game.getPairing());
        Player enemyPlayer = pairingService.getEnemyOf(game.getPairing(), currentPlayer);
        int currentPlayerIndex = pairingService.getCurrentPlayerIndex(game.getPairing());



        switch (currentPhase){
            case PUT:
                LinkedList<Put> possiblePuts = boardService.getPossiblePuts(game.getBoard(), currentPlayerIndex);
                for (Put put : possiblePuts){
                    Board clonedBoard = game.getBoard().clone();
                    boardService.putStone(clonedBoard, put, currentPlayerIndex);
                    System.out.println(clonedBoard);
                    Game clonedGame = game.clone();
                    clonedGame.setBoard(clonedBoard);
                    GameNode gameNode = new GameNode(clonedBoard, put, currentPlayerIndex, parent, evaluateScore(clonedBoard, currentPlayerIndex));
                    playerService.increasePutStones(currentPlayer);
                    if (!boardService.isPositionPartOfMorris(clonedBoard, put.getPutPosition())){
                        gameStateService.increaseRound(clonedGame.getGameState());
                        pairingService.changeTurn(clonedGame.getPairing());
                        playerService.changeToWaitPhase(currentPlayer);
                        playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                    } else {
                        playerService.changeToKillPhase(currentPlayer);
                    }
                    recursiveAlphaBeta(clonedGame, ownIndex, maxLevel, currentLevel + 1, gameNode);
                }
                break;
            case MOVE:
                LinkedList<Move> possibleMoves = boardService.getPossibleMoves(game.getBoard(), currentPlayerIndex);
                for (Move move : possibleMoves){
                    Board clonedBoard = game.getBoard().clone();
                    boardService.moveStone(clonedBoard, move, currentPlayerIndex);
                    System.out.println(clonedBoard);
                    Game clonedGame = game.clone();
                    clonedGame.setBoard(clonedBoard);
                    GameNode gameNode = new GameNode(clonedBoard, move, currentPlayerIndex, parent, evaluateScore(clonedBoard, currentPlayerIndex));
                    if (!boardService.isPositionPartOfMorris(clonedBoard, move.getTo())){
                        gameStateService.increaseRound(clonedGame.getGameState());
                        pairingService.changeTurn(clonedGame.getPairing());
                        playerService.changeToWaitPhase(currentPlayer);
                        playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                    } else {
                        playerService.changeToKillPhase(currentPlayer);
                    }
                    recursiveAlphaBeta(clonedGame, ownIndex, maxLevel, currentLevel + 1, gameNode);
                }
                break;
            case KILL:
                LinkedList<Kill> possibleKills = boardService.getPossibleKills(game.getBoard(), currentPlayerIndex);
                for (Kill kill : possibleKills){
                    Board clonedBoard = game.getBoard().clone();
                    boardService.killStone(clonedBoard, kill);
                    System.out.println(clonedBoard);
                    Game clonedGame = game.clone();
                    clonedGame.setBoard(clonedBoard);
                    GameNode gameNode = new GameNode(clonedBoard, kill, currentPlayerIndex, parent, evaluateScore(clonedBoard, currentPlayerIndex));
                    pairingService.changeTurn(clonedGame.getPairing());
                    gameStateService.increaseRound(clonedGame.getGameState());
                    playerService.setPhase(currentPlayer, PHASE.WAIT);
                    playerService.increaseKilledStones(currentPlayer);
                    playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                    playerService.increaseLostStones(enemyPlayer);
                    recursiveAlphaBeta(clonedGame, ownIndex, maxLevel, currentLevel + 1, gameNode);
                }
                break;

        }




    }




    private int evaluateScore(Board board, int playerIndex){
        int ownStones = boardService.getNumberOfStonesOfPlayerWithIndex(board, playerIndex);
        int enemyIndex = playerIndex == 1 ? 2 : 1;
        int enemyStones = boardService.getNumberOfStonesOfPlayerWithIndex(board, enemyIndex);
        int score = ownStones - enemyStones;

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
