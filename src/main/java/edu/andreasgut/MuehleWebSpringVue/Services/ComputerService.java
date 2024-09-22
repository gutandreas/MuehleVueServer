package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
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


    public ComputerService(BoardService boardService) {
        this.boardService = boardService;
    }

    public Put calculatePut(Game game, int playerIndex){

        StandardComputerPlayer standardComputerPlayer = (StandardComputerPlayer) game.getPairing().getPlayerByIndex(playerIndex);
        int level = standardComputerPlayer.getLevel();

        switch (level){
            case 1:
                return getRandomPut(game, playerIndex);
            case 2:
                return getMinMaxPut(game, playerIndex);
            default:
                return null;
        }


    }

    private Put getRandomPut(Game game, int playerIndex){
        logger.info("Neuer zufälliger Put wird berechnet");
        LinkedList<Put> possiblePuts = boardService.getPossiblePuts(game.getBoard(), playerIndex);
        return possiblePuts.get(new Random().nextInt(possiblePuts.size()));
    }

    private Put getMinMaxPut(Game game, int playerIndex){
        LinkedList<Put> possiblePuts = boardService.getPossiblePuts(game.getBoard(), playerIndex);
        Put bestPut = null;
        int bestScore = Integer.MIN_VALUE;
        for (Put put : possiblePuts){
            Board board = new Board(game.getBoard());
            boardService.putStone(board, put, playerIndex);
            int score = evaluateScore(board, playerIndex);
            System.out.println("Aktueller Score bei Put: " + score);
            if (score > bestScore){
                bestScore = score;
                bestPut = put;
                System.out.println("Neuer bester Score bei Put: " + score);
            }
        }

        return bestPut;


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
