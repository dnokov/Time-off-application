package com.learning.timeOffManagement.repository;

import com.learning.timeOffManagement.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTitle(String title);

    Boolean existsByTitle(String title);
}
