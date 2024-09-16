package edu.andreasgut.MuehleWebSpringVue.Repositories;


import edu.andreasgut.MuehleWebSpringVue.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

    Game findByGameCode(String gameCode);
    LinkedList<Game> findAll();

    LinkedList<Game> findByGameState_FinishedFalse();

    void deleteGameByGameCode(String gameCode);

    boolean existsByGameCode(String gameCode);






}