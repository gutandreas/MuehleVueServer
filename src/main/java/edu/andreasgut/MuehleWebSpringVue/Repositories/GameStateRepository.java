package edu.andreasgut.MuehleWebSpringVue.Repositories;

import edu.andreasgut.MuehleWebSpringVue.Models.GamePersistent;
import edu.andreasgut.MuehleWebSpringVue.Models.GameStatePersistent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedList;

public interface GameStateRepository extends JpaRepository<GameStatePersistent, Long> {

}
