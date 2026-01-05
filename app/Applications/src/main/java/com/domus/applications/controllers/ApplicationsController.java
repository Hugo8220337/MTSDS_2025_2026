package com.domus.applications.controllers;

import com.domus.applications.dto.request.SubmitApplicationRequestDto;
import com.domus.applications.dto.request.UpdateApplicationStatusRequestDto;
import com.domus.applications.dto.response.ApplicationResponseDto;
import com.domus.applications.entities.Application;
import com.domus.applications.services.ApplicationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationsController {

    private final ModelMapper mapper;
    private final ApplicationService applicationService;

    public ApplicationsController(ModelMapper mapper, ApplicationService applicationService) {
        this.mapper = mapper;
        this.applicationService = applicationService;
    }

    @PostMapping("create-draft")
    public ResponseEntity<ApplicationResponseDto> createDraft(@RequestBody SubmitApplicationRequestDto draftApplication) {
        log.info("createDraft called with data: {}", draftApplication);

        Application newApplication = applicationService
                .createDraftApplication(draftApplication);
        ApplicationResponseDto applicationResponseDto = mapper.map(newApplication, ApplicationResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationResponseDto);
    }

    @PostMapping("/{applicationId}/submit")
    public ResponseEntity<ApplicationResponseDto> submitApplication(
            @PathVariable Long applicationId
    ) {
        log.info("submitApplication called with applicationId: {}", applicationId);

        Application submittedApplication = applicationService
                .submitApplication(applicationId);
        ApplicationResponseDto applicationResponseDto = mapper.map(submittedApplication, ApplicationResponseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto> getApplication(@PathVariable Long id) {
        log.info("getApplication called with id: {}", id);

        Application application = applicationService.getApplicationById(id);
        ApplicationResponseDto applicationResponseDto = mapper.map(application, ApplicationResponseDto.class);
        return ResponseEntity.ok(applicationResponseDto);
    }

    @PutMapping("/{applicationId}/update-state")
    public ResponseEntity<ApplicationResponseDto> updateState(
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequestDto stateUpdate) {

        Application updatedApplication = applicationService.updateApplicationState(applicationId, stateUpdate);
        ApplicationResponseDto responseDto = mapper.map(updatedApplication, ApplicationResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}/update-draft")
    public ResponseEntity<ApplicationResponseDto> updateDraft(
            @PathVariable Long id,
            @RequestBody SubmitApplicationRequestDto draftApplication) {
        log.info("updateDraft called with data: {}", draftApplication);
        
        Application updatedApplication = applicationService
                .updateDraftApplication(id, draftApplication);
        ApplicationResponseDto applicationResponseDto = mapper.map(updatedApplication, ApplicationResponseDto.class);
        return ResponseEntity.ok(applicationResponseDto);
    }
}
