package edu.andreasgut.MuehleWebSpringVue.Repositories;

import edu.andreasgut.MuehleWebSpringVue.Models.GameState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStateRepository extends JpaRepository<GameState, Long> {

}
