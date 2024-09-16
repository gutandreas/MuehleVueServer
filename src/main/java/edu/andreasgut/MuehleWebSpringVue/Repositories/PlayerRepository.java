package edu.andreasgut.MuehleWebSpringVue.Repositories;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
