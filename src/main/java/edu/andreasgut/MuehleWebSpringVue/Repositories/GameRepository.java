package edu.andreasgut.MuehleWebSpringVue.Repositories;


import edu.andreasgut.MuehleWebSpringVue.Models.*;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.Map;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

    LinkedList<Game> findByGameCode(String gameCode);
    LinkedList<Game> findAll();
    LinkedList<Game> findByFinishedFalse();

    void deleteGameByGameCode(String gameCode);







}