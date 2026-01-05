package com.domus.applications.controllers;

import com.domus.applications.dto.request.CompetitionRequestDto;
import com.domus.applications.dto.response.CompetitionResponseDto;
import com.domus.applications.services.CompetitionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/applications/competitions")
public class CompetitionsController {

    private final ModelMapper mapper;
    private final CompetitionService competitionService;

    public CompetitionsController(ModelMapper mapper, CompetitionService competitionService) {
        this.mapper = mapper;
        this.competitionService = competitionService;
    }

    @PostMapping
    public ResponseEntity<CompetitionResponseDto> createCompetition(@RequestBody CompetitionRequestDto competition) {
        log.info("createCompetition called with competition: {}", competition);

        var createdCompetition = competitionService.createCompetition(competition);
        var responseDto = mapper.map(createdCompetition, CompetitionResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CompetitionResponseDto>> listCompetitions() {
        log.info("listCompetitions called");

        var competitions = competitionService.getAllCompetitions();
        var responseDtos = competitions.stream()
                .map(competition -> mapper.map(competition, CompetitionResponseDto.class))
                .toList();
        return ResponseEntity.ok(responseDtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CompetitionResponseDto> getCompetitionById(@PathVariable Long id) {
        log.info("getCompetition called with id: {}", id);

        var competition = competitionService.getCompetitionById(id);
        var responseDto = mapper.map(competition, CompetitionResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<CompetitionResponseDto> updateCompetition(
            @PathVariable Long id,
            @RequestBody CompetitionRequestDto competition) {
        log.info("updateCompetition called with id: {} and competition: {}", id, competition);
        
        var updatedCompetition = competitionService.updateCompetition(id, competition);
        var responseDto = mapper.map(updatedCompetition, CompetitionResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }
}