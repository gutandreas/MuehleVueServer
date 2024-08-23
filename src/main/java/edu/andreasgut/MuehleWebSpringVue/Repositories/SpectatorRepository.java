package edu.andreasgut.MuehleWebSpringVue.Repositories;

import edu.andreasgut.MuehleWebSpringVue.Models.PlayerAndSpectator.Spectator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpectatorRepository extends JpaRepository<Spectator, UUID> {
}
