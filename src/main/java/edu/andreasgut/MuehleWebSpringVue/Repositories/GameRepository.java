package edu.andreasgut.MuehleWebSpringVue.Repositories;


import edu.andreasgut.MuehleWebSpringVue.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

    Game findByGameCode(String gameCode);
    LinkedList<Game> findAll();

    LinkedList<Game> findByGameState_FinishedFalse();

    void deleteGameByGameCode(String gameCode);

    boolean existsByGameCode(String gameCode);

    @Query("SELECT COUNT(g) FROM Game g " +
            "JOIN g.pairing p " +
            "JOIN p.player1 pl1 " +
            "JOIN p.player2 pl2 " +
            "JOIN GameState gs ON g.gameState = gs " +
            "WHERE gs.finished = true " +
            "AND ((gs.winnerIndex = 1 AND TYPE(pl1) = StandardComputerPlayer) " +
            "OR (gs.winnerIndex = 2 AND TYPE(pl2) = StandardComputerPlayer))")
    Integer countGamesWonByStandardComputerPlayer();

    @Query("SELECT COUNT(g) FROM Game g " +
            "JOIN g.pairing p " +
            "JOIN p.player1 pl1 " +
            "JOIN p.player2 pl2 " +
            "JOIN GameState gs ON g.gameState = gs " +
            "WHERE gs.finished = true " +
            "AND (TYPE(pl1) = StandardComputerPlayer OR TYPE(pl2) = StandardComputerPlayer)")
    Integer countFinishedGamesByStandardComputerPlayer();






}