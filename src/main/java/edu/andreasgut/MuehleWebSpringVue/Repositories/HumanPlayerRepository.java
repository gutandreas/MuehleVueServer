package edu.andreasgut.MuehleWebSpringVue.Repositories;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.HumanPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.UUID;

@Repository
public interface HumanPlayerRepository extends JpaRepository<HumanPlayer, UUID> {


    @Query("SELECT hp FROM HumanPlayer hp WHERE EXISTS (" +
            "SELECT g FROM Game g WHERE g.pairing.player1 = hp AND g.gameState.finished = false" +
            ") OR EXISTS (" +
            "SELECT g FROM Game g WHERE g.pairing.player2 = hp AND g.gameState.finished = false)")
    LinkedList<HumanPlayer> findAllWithUnfinishedGames();

}