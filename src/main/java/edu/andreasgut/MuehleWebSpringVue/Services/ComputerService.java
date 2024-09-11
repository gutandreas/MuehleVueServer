package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.StandardComputerPlayer;

import java.util.LinkedList;
import java.util.Random;

public class ComputerService {

    BoardService boardService;

    public ComputerService(BoardService boardService) {
        this.boardService = boardService;
    }

    public Put calculatePut(StandardComputerPlayer standardComputerPlayer, Board board, int playerIndex){
        LinkedList<Put> possiblePuts = boardService.getPossiblePuts(board, playerIndex);
        return possiblePuts.get(new Random().nextInt(possiblePuts.size()));
    }

    public Move calculateMove(StandardComputerPlayer standardComputerPlayer, Board board, int playerIndex){
        LinkedList<Move> possibleMoves = boardService.getPossibleMoves(board, playerIndex);
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

    public Jump calculateJumo(StandardComputerPlayer standardComputerPlayer, Board board, int playerIndex){
        LinkedList<Jump> possibleJumps = boardService.getPossibleJumps(board, playerIndex);
        return possibleJumps.get(new Random().nextInt(possibleJumps.size()));
    }

    public Kill calculateKill(StandardComputerPlayer standardComputerPlayer, Board board, int playerIndex){
        LinkedList<Kill> possibleKills = boardService.getPossibleKills(board, playerIndex);
        return possibleKills.get(new Random().nextInt(possibleKills.size()));
    }



}
