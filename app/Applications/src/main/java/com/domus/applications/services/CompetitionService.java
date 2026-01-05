package com.domus.applications.services;

import com.domus.applications.dto.request.CompetitionRequestDto;
import com.domus.applications.entities.Competition;
import com.domus.applications.repositories.CompetitionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionService {
    private final ModelMapper mapper;
    private final CompetitionRepository competitionRepository;

    public CompetitionService(ModelMapper mapper, CompetitionRepository competitionRepository) {
        this.mapper = mapper;
        this.competitionRepository = competitionRepository;
    }

    public Competition createCompetition(CompetitionRequestDto competition) {
        Competition competitionEntity = mapper.map(competition, Competition.class);
        return competitionRepository.save(competitionEntity);
    }

    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAll();
    }

    public Competition getCompetitionById(Long id) {
        return competitionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Competition not found")
        );
    }

    public Competition updateCompetition(Long id, CompetitionRequestDto competition) {
        Competition existingCompetition = getCompetitionById(id);
        mapper.map(competition, existingCompetition);
        return competitionRepository.save(existingCompetition);
    }
}
