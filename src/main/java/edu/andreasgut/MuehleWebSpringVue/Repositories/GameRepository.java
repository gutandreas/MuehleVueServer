package edu.andreasgut.MuehleWebSpringVue.Repositories;


import edu.andreasgut.MuehleWebSpringVue.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface GameRepository extends JpaRepository<GamePersistent, Long>{

    GamePersistent findByGameCode(String gameCode);
    LinkedList<GamePersistent> findAll();

    LinkedList<GamePersistent> findByGameState_FinishedFalse();

    void deleteGameByGameCode(String gameCode);

    boolean existsByGameCode(String gameCode);






}