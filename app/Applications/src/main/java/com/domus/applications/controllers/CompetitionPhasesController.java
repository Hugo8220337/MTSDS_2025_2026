package com.domus.applications.controllers;

import com.domus.applications.dto.request.CompetitionPhaseRequestDto;
import com.domus.applications.dto.request.DgesImportRequestDto;
import com.domus.applications.dto.response.ApplicationResponseDto;
import com.domus.applications.dto.response.CompetitionPhaseResponseDto;
import com.domus.applications.entities.CompetitionPhase;
import com.domus.applications.services.CompetitionPhaseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/applications/competitions/{competitionId}/phases")
public class CompetitionPhasesController {
    private final ModelMapper mapper;
    private final CompetitionPhaseService competitionPhaseService;

    public CompetitionPhasesController(ModelMapper mapper, CompetitionPhaseService competitionPhaseService) {
        this.mapper = mapper;
        this.competitionPhaseService = competitionPhaseService;
    }

    @PostMapping
    public ResponseEntity<CompetitionPhaseResponseDto> createPhase(
            @PathVariable Long competitionId,
            @RequestBody CompetitionPhaseRequestDto phase) {
        log.info("createPhase called with competitionId: {} and phase: {}", competitionId, phase);

        CompetitionPhase createdPhase = competitionPhaseService.createPhase(competitionId, phase);
        CompetitionPhaseResponseDto responseDto = mapper.map(createdPhase, CompetitionPhaseResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CompetitionPhaseResponseDto>> listPhasesByCompetitionId(
            @PathVariable Long competitionId) {
        log.info("listPhases called with competitionId: {}", competitionId);

        List<CompetitionPhase> phases = competitionPhaseService.getPhasesByCompetitionId(competitionId);
        List<CompetitionPhaseResponseDto> responseDtos = phases.stream()
                .map(phase -> mapper.map(phase, CompetitionPhaseResponseDto.class))
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping("/{phaseId}/dges-import")
    public ResponseEntity<List<ApplicationResponseDto>> importDges(
            @PathVariable Long competitionId,
            @PathVariable Long phaseId,
            @RequestBody DgesImportRequestDto importRequest) {
        log.info("importDges called with competitionId: {}, phaseId: {} and importRequest: {}",
                competitionId, phaseId, importRequest);
        
        List<ApplicationResponseDto> importedApplications =
                competitionPhaseService.importDgesData(competitionId, phaseId, importRequest);
        return ResponseEntity.ok(importedApplications);
    }
}