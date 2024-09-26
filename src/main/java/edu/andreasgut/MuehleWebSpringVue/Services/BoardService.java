package edu.andreasgut.MuehleWebSpringVue.Services;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Jump;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Kill;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Move;
import edu.andreasgut.MuehleWebSpringVue.Models.GameActions.Put;
import edu.andreasgut.MuehleWebSpringVue.Models.POSITIONSTATE;
import edu.andreasgut.MuehleWebSpringVue.Models.Position;
import edu.andreasgut.MuehleWebSpringVue.Repositories.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;

@Service
public class BoardService {

    BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public POSITIONSTATE getStateOfPosition(Board board, Position position){
        return board.getBoardPositionsStates()[position.getRing()][position.getField()];
    }



    public int getNumberOfStates(Board board, POSITIONSTATE positionstate){
        int counter = 0;
        for (int i = 0; i < board.getBoardPositionsStates().length; i++) {
            for (int j = 0; j < board.getBoardPositionsStates()[0].length; j++) {
                if (board.getBoardPositionsStates()[i][j] == positionstate){
                    counter++;
                }
            }
        }
        return counter;

    }

    public int getNumberOfStonesOfPlayerWithIndex(Board board, int index){

        POSITIONSTATE state = index == 1 ? POSITIONSTATE.PLAYER1 : POSITIONSTATE.PLAYER2;
        return getNumberOfStates(board, state);

    }

    public boolean isThisPositionOccupiedByPlayerWithIndex(Board board, int index, Position position){
        POSITIONSTATE positionstateInBoard = board.getBoardPositionsStates()[position.getRing()][position.getField()];
        POSITIONSTATE expectedPositionState = index == 1 ? POSITIONSTATE.PLAYER1 : POSITIONSTATE.PLAYER2;

        return positionstateInBoard == expectedPositionState;
    }

    @Transactional
    public void putStoneAndSave(Board board, Put put, int playerIndex) {
        setStoneOnPositionInArray(board, put.getPutPosition(), playerIndex);
        saveBoard(board);
    }

    public void putStone(Board board, Put put, int playerIndex) {
        setStoneOnPositionInArray(board, put.getPutPosition(), playerIndex);}


    @Transactional
    public void moveStone(Board board, Move move, int playerIndex) {
        setStoneOnPositionInArray(board, move.getTo(), playerIndex);
        removeStoneFromPositionInArray(board, move.getFrom());
        saveBoard(board);
    }

    @Transactional
    public void jumpStone(Board board, Jump jump, int playerIndex) {
        setStoneOnPositionInArray(board, jump.getTo(), playerIndex);
        removeStoneFromPositionInArray(board, jump.getFrom());
        saveBoard(board);
    }

    @Transactional
    public void killStone(Board board, Kill kill) {
        removeStoneFromPositionInArray(board, kill.getKillPosition());
        saveBoard(board);
    }

    private void saveBoard(Board board){
            boardRepository.save(board);
    }


    public boolean isPositionFree(Board board, Position position){
        return board.getBoardPositionsStates()[position.getRing()][position.getField()] == POSITIONSTATE.FREE;
    }

    public boolean arePositionsNeighbours(Position position1, Position position2) {


        boolean neighboursInRing = (position1.getRing() == position2.getRing() && Math.abs(position1.getField() - position2.getField()) == 1)
                || (position1.getRing() == position2.getRing() && Math.abs(position1.getField() - position2.getField()) == 7);

        boolean neighboursBetweenRings = position1.getField() % 2 == 1 && position1.getField() == position2.getField()
                && Math.abs(position1.getRing() - position2.getRing()) == 1;

        return neighboursInRing || neighboursBetweenRings;
    }



    private void removeStoneFromPositionInArray(Board board, Position position){
        board.getBoardPositionsStates()[position.getRing()][position.getField()] = POSITIONSTATE.FREE;
    }

    private void setStoneOnPositionInArray(Board board, Position position, int playerIndex){
        POSITIONSTATE positionstate = playerIndex == 1 ? POSITIONSTATE.PLAYER1 : POSITIONSTATE.PLAYER2;
        board.getBoardPositionsStates()[position.getRing()][position.getField()] = positionstate;
    }

    public boolean isPositionPartOfMorris(Board board, Position position) {

        boolean cornerField = position.getField() % 2 == 0;
        boolean morris;
        POSITIONSTATE positionstate = board.getBoardPositionsStates()[position.getRing()][position.getField()];

        if (cornerField) {
            morris = isPositionPartOfMorrisInRingFromCornerField(board, position, positionstate);
        } else {
            morris = isPositionPartOfMorrisInRingFromCenterField(board, position, positionstate) || isPositionPartOfMorrisBetweenRings(board, position, positionstate);
        }

        return morris;
    }

    public boolean areAllStonesPartOfMorris(Board board, int playerIndex){
        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                POSITIONSTATE positionstate = playerIndex == 1 ? POSITIONSTATE.PLAYER1 : POSITIONSTATE.PLAYER2;
                if (board.getBoardPositionsStates()[ring][field] == positionstate){
                    if(!isPositionPartOfMorris(board, new Position(ring, field))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isPositionPartOfMorrisInRingFromCornerField(Board board, Position position, POSITIONSTATE positionstate) {

        boolean morrisUpwards = positionstate == board.getBoardPositionsStates()[position.getRing()][(position.getField() + 1) % 8]
                && positionstate == board.getBoardPositionsStates()[position.getRing()][(position.getField() + 2) % 8];
        boolean morrisDownwards = positionstate == board.getBoardPositionsStates()[position.getRing()][(position.getField() + 6) % 8]
                && positionstate == board.getBoardPositionsStates()[position.getRing()][(position.getField() + 7) % 8];

        return morrisUpwards || morrisDownwards;
    }


    private boolean isPositionPartOfMorrisInRingFromCenterField(Board board, Position position, POSITIONSTATE positionstate) {
        return positionstate == board.getBoardPositionsStates()[position.getRing()][(position.getField() + 1) % 8]
                && positionstate == board.getBoardPositionsStates()[position.getRing()][(position.getField() + 7) % 8];
    }


    private boolean isPositionPartOfMorrisBetweenRings(Board board, Position position, POSITIONSTATE positionstate) {
        return positionstate == board.getBoardPositionsStates()[(position.getRing() + 1) % 3][position.getField()]
                && positionstate == board.getBoardPositionsStates()[(position.getRing() + 2) % 3][position.getField()];
    }

    public LinkedList<Put> getPossiblePuts(Board board) {
        LinkedList<Put> possiblePuts = new LinkedList<>();
        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                if (isPositionFree(board, new Position(ring, field))) {
                    possiblePuts.add(new Put(new Position(ring, field)));
                }
            }
        }
        return possiblePuts;
    }

    public LinkedList<Kill> getPossibleKills(Board board, int playerIndex) {
        LinkedList<Kill> possibleKills = new LinkedList<>();
        int enemyIndex = playerIndex == 1 ? 2 : 1;
        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                Position killposition = new Position(ring, field);
                boolean enemyPosition = isThisPositionOccupiedByPlayerWithIndex(board, enemyIndex, killposition);
                boolean inMorris = isPositionPartOfMorris(board, killposition);
                boolean allStonesInMorris = areAllStonesPartOfMorris(board, enemyIndex);
                if (enemyPosition && (!inMorris || allStonesInMorris)) {
                    possibleKills.add(new Kill(killposition));
                }
            }
        }
        return possibleKills;
    }

    public  LinkedList<Move> getPossibleMoves(Board board, int playerIndex) {
        LinkedList<Move> possibleMoves = new LinkedList<>();

        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {

                Position from = new Position(ring, field);
                if (isThisPositionOccupiedByPlayerWithIndex(board, playerIndex, from)) {
                    if (isPositionFree(board, new Position(ring, (field + 1) % 8))) {
                        Move move = new Move(from, new Position(ring, (field + 1) % 8));
                        possibleMoves.add(move);
                    }
                    if (isPositionFree(board, new Position(ring, (field + 7) % 8))) {
                        Move move = new Move(from, new Position(ring, (field + 7) % 8));
                        possibleMoves.add(move);
                    }
                    if (field % 2 == 1 && (ring == 0 || ring == 1) && isPositionFree(board, new Position(ring + 1, field))) {
                        Move move = new Move(from, new Position(ring + 1, field));
                        possibleMoves.add(move);
                    }
                    if (field % 2 == 1 && (ring == 1 || ring == 2) && isPositionFree(board, new Position(ring - 1, field))) {
                        Move move = new Move(from, new Position(ring - 1, field));
                        possibleMoves.add(move);
                    }
                }
            }
        }
        return possibleMoves;
    }

    public  LinkedList<Jump> getPossibleJumps(Board board, int playerIndex) {
        LinkedList<Jump> possibleJumps = new LinkedList<>();

        for (int ringFrom = 0; ringFrom < 3; ringFrom++) {
            for (int fieldFrom = 0; fieldFrom < 8; fieldFrom++) {

                Position from = new Position(ringFrom, fieldFrom);
                if (isThisPositionOccupiedByPlayerWithIndex(board, playerIndex, from)) {
                    for (int ringTo = 0; ringTo < 3; ringTo++) {
                        for (int fieldTo = 0; fieldTo < 8; fieldTo++) {
                            Jump jump = new Jump(from, new Position(ringTo, fieldTo));
                            possibleJumps.add(jump);
                        }

                    }
                }
            }
        }

        return possibleJumps;
    }
}
