package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.AlphaBeta.GameNode;
import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.*;
import edu.andreasgut.MuehleWebSpringVue.Models.PHASE;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;
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





    public Put calculatePut(Game game, int playerIndex){

        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) game.getPairing().getPlayerByIndex(playerIndex);
        int level = standardComputerPlayer.getLevel();


        switch (level){
            case 0:
                return caclutaRandomPut(game.getBoard());
            case 1:
                return (Put) executeAlphaBeta(game, playerIndex, 2);
            case 2:
                return (Put) executeAlphaBeta(game, playerIndex, 4);
            default:
                return null;
        }


    }

    public Move calculateMove(Game game, int playerIndex){
        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) game.getPairing().getPlayerByIndex(playerIndex);
        int level = standardComputerPlayer.getLevel();


        switch (level){
            case 0:
                return calculateRandomMove(game.getBoard(), playerIndex);
            case 1:
                return (Move) executeAlphaBeta(game, playerIndex, 2);
            case 2:
                return (Move) executeAlphaBeta(game, playerIndex, 4);
            default:
                return null;
        }
    }

    public Kill calculateKill(Game game, int playerIndex){
        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) game.getPairing().getPlayerByIndex(playerIndex);
        int level = standardComputerPlayer.getLevel();


        switch (level){
            case 0:
                return calculateRandomKill(game.getBoard(), playerIndex);
            case 1:
                return (Kill) executeAlphaBeta(game, playerIndex, 2);
            case 2:
                return (Kill) executeAlphaBeta(game, playerIndex, 4);
            default:
                return null;
        }
    }

    public Jump calculateJump(Game game, int playerIndex){
        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) game.getPairing().getPlayerByIndex(playerIndex);
        int level = standardComputerPlayer.getLevel();


        switch (level){
            case 0:
                return calculateRandomJump(game.getBoard(), playerIndex);
            case 1:
                return (Jump) executeAlphaBeta(game, playerIndex, 2);
            case 2:
                return (Jump) executeAlphaBeta(game, playerIndex, 4);
            default:
                return null;
        }
    }



    private GameAction executeAlphaBeta(Game game, int ownIndex, int maxLevel){

        Game clonedGame = game.clone();

        int currentLevel = 0;


        GameNode root = new GameNode(clonedGame.getBoard(), null, 0, null, 0);
        PHASE currentPhase = playerService.getPhase(pairingService.getCurrentPlayer(game.getPairing()));

        recursiveAlphaBeta(clonedGame, ownIndex, maxLevel, currentLevel, currentPhase, root, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

        root.printTree();

        System.out.println("Erreichter Score: " + root.getScore());
        return root.getBestAction();
    }

    private int recursiveAlphaBeta(Game game, int ownIndex, int maxLevel, int currentLevel, PHASE currentPhase, GameNode node, int alpha, int beta, boolean isMaximizingPlayer) {
        // Basisfall: Maximale Suchtiefe erreicht

        if (isGameOver(game)){
            node.setScore(gameStateService.getWinnerIndex(game.getGameState()) == ownIndex ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        }

        if (currentLevel == maxLevel) {
            int score = evaluateScore(game.getBoard(), ownIndex);  // Bewertungsfunktion
            node.setScore(score);  // Setze den Wert im Knoten
            return score;
        }

        // Behandle alle möglichen Aktionen basierend auf der Phase
        LinkedList<? extends GameAction> possibleActions = getPossibleActionsForPhase(game, currentPhase);

        GameAction bestAction = null;
        int bestScore = isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (GameAction action : possibleActions) {
            Game clonedGame = game.clone();
            executeGameAction(clonedGame, action, currentPhase);

            updateGameStateAfterAction(clonedGame, action, currentPhase);
            boolean maximizing = pairingService.getCurrentPlayerIndex(clonedGame.getPairing()) == ownIndex;
            PHASE nextPhase = playerService.getPhase(pairingService.getCurrentPlayer(clonedGame.getPairing()));

            GameNode child = new GameNode(clonedGame.getBoard(), action, pairingService.getCurrentPlayerIndex(clonedGame.getPairing()), node, 0);
            int nextMaxLevel = nextPhase == PHASE.KILL ? maxLevel + 1 : maxLevel;
            int score = recursiveAlphaBeta(clonedGame, ownIndex, nextMaxLevel, currentLevel + 1, nextPhase, child, alpha, beta, maximizing);

            if (isMaximizingPlayer) {
                if (score > bestScore) {
                    bestScore = score;
                    bestAction = action;
                }
                alpha = Math.max(alpha, score);
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    bestAction = action;
                }
                beta = Math.min(beta, score);
            }

            if (alpha >= beta) {
                break;
            }

        }

        node.setScore(bestScore);
        node.setBestAction(bestAction);

        return bestScore;
    }

    private void updateGameStateAfterAction(Game game, GameAction action, PHASE currentPhase) {

        if (isGameOver(game)){
            gameStateService.finishGame(game.getGameState());
            gameStateService.setWinner(game.getGameState(), pairingService.getCurrentPlayerIndex(game.getPairing()));
        }

        // Aktualisiert den Spielzustand nach einer Aktion und bereitet das Spiel für die nächste Phase vor
        Player currentPlayer = pairingService.getCurrentPlayer(game.getPairing());
        Player enemyPlayer = pairingService.getEnemyOf(game.getPairing(), currentPlayer);

        switch (currentPhase) {
            case PUT:
                if (!boardService.isPositionPartOfMorris(game.getBoard(), ((Put) action).getPutPosition())) {
                    gameStateService.increaseRound(game.getGameState());
                    pairingService.changeTurn(game.getPairing());
                    playerService.changeToWaitPhase(currentPlayer);
                    playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                } else {
                    playerService.changeToKillPhase(currentPlayer);  // Spieler darf einen Stein schlagen
                }
                break;
            case MOVE:
                if (!boardService.isPositionPartOfMorris(game.getBoard(), ((Move) action).getTo())) {
                    gameStateService.increaseRound(game.getGameState());
                    pairingService.changeTurn(game.getPairing());
                    playerService.changeToWaitPhase(currentPlayer);
                    playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                } else {
                    playerService.changeToKillPhase(currentPlayer);  // Spieler darf einen Stein schlagen
                }
                break;
            case KILL:
                gameStateService.increaseRound(game.getGameState());
                pairingService.changeTurn(game.getPairing());
                playerService.changeToWaitPhase(currentPlayer);
                playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                break;
            case JUMP:
                if (!boardService.isPositionPartOfMorris(game.getBoard(), ((Jump) action).getTo())) {
                    gameStateService.increaseRound(game.getGameState());
                    pairingService.changeTurn(game.getPairing());
                    playerService.changeToWaitPhase(currentPlayer);
                    playerService.setPhase(enemyPlayer, playerService.getPhaseIfPutMoveOrJump(enemyPlayer));
                } else {
                    playerService.changeToKillPhase(currentPlayer);  // Spieler darf einen Stein schlagen
                }
                break;
        }


    }

    private LinkedList<? extends GameAction> getPossibleActionsForPhase(Game game, PHASE currentPhase) {
        // Gib die möglichen Züge basierend auf der Phase zurück
        switch (currentPhase) {
            case PUT:
                return boardService.getPossiblePuts(game.getBoard());
            case MOVE:
                return boardService.getPossibleMoves(game.getBoard(), pairingService.getCurrentPlayerIndex(game.getPairing()));
            case KILL:
                return boardService.getPossibleKills(game.getBoard(), pairingService.getCurrentPlayerIndex(game.getPairing()));
            case JUMP:
                return boardService.getPossibleJumps(game.getBoard(), pairingService.getCurrentPlayerIndex(game.getPairing()));
            default:
                return new LinkedList<>();
        }
    }

    private void executeGameAction(Game game, GameAction action, PHASE currentPhase) {
        // Führt die entsprechende Aktion auf dem Spielbrett aus (Put, Move, Kill)
        int currentPlayerIndex = pairingService.getCurrentPlayerIndex(game.getPairing());

        switch (currentPhase) {
            case PUT:
                boardService.putStone(game.getBoard(), (Put) action, currentPlayerIndex);
                break;
            case MOVE:
                boardService.moveStone(game.getBoard(), (Move) action, currentPlayerIndex);
                break;
            case KILL:
                boardService.killStone(game.getBoard(), (Kill) action);
                break;
            case JUMP:
                boardService.jumpStone(game.getBoard(), (Jump) action, currentPlayerIndex);
                break;
        }
    }

    private int evaluateScore(Board board, int playerIndex) {
        int stonesWeight = 100;
        int movesWeight = 1;

        // Anzahl der Steine und Züge für den eigenen Spieler
        int ownStones = boardService.getNumberOfStonesOfPlayerWithIndex(board, playerIndex);
        int ownMoves = boardService.getPossibleMoves(board, playerIndex).size();

        // Anzahl der Steine und Züge für den Gegner
        int enemyIndex = playerIndex == 1 ? 2 : 1;
        int enemyStones = boardService.getNumberOfStonesOfPlayerWithIndex(board, enemyIndex);
        int enemyMoves = boardService.getPossibleMoves(board, enemyIndex).size();

        // Berechnung der Punktzahl
        return stonesWeight * (ownStones - enemyStones) + movesWeight * (ownMoves - enemyMoves);
    }

    private boolean isGameOver(Game game){
        int numberOfEnemysStones = boardService.getNumberOfStonesOfPlayerWithIndex(game.getBoard(), game.getPairing().getCurrentPlayerIndex());
        int round = gameStateService.getRound(game.getGameState());
        boolean lostBeacauseOfNumberOfStones = round > 18 && numberOfEnemysStones < 3;
        boolean lostBeacauseOfLockedIn = playerService.isPlayerInMovePhase(game.getPairing().getCurrentPlayer()) && boardService.hasPlayerNoPossibilitiesToMove(game.getBoard(), game.getPairing().getCurrentPlayerIndex());
        return lostBeacauseOfNumberOfStones || lostBeacauseOfLockedIn;
    }

    private Put caclutaRandomPut(Board board){
        logger.info("Neuer zufälliger Put wird berechnet");
        LinkedList<Put> possiblePuts = boardService.getPossiblePuts(board);
        return possiblePuts.get(new Random().nextInt(possiblePuts.size()));
    }

    public Move calculateRandomMove(Board board, int playerIndex) {
        logger.info("Neuer Move wird berechnet");
        LinkedList<Move> possibleMoves = boardService.getPossibleMoves(board, playerIndex);
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

    public Jump calculateRandomJump(Board board, int playerIndex) {
        logger.info("Neuer Jump wird berechnet");
        LinkedList<Jump> possibleJumps = boardService.getPossibleJumps(board, playerIndex);
        return possibleJumps.get(new Random().nextInt(possibleJumps.size()));
    }

    public Kill calculateRandomKill(Board board, int playerIndex) {
        logger.info("Neuer Kill wird berechnet");
        LinkedList<Kill> possibleKills = boardService.getPossibleKills(board, playerIndex);
        return possibleKills.get(new Random().nextInt(possibleKills.size()));
    }


}
