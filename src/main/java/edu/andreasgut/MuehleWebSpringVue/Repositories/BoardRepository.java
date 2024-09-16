package edu.andreasgut.MuehleWebSpringVue.Repositories;

import edu.andreasgut.MuehleWebSpringVue.Models.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {

}