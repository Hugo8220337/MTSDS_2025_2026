package com.domus.applications.repositories;

import com.domus.applications.entities.CompetitionPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionPhaseRepository extends JpaRepository<CompetitionPhase,Long> {
    List<CompetitionPhase> findByCompetitionId(Long competitionId);
}
