package com.sumerge.survey.controller;

// SurveyFormController.java
import com.sumerge.survey.request.CreateFormRequest;
import com.sumerge.survey.request.SectionStateRequest;
import com.sumerge.survey.entity.SurveyForm;
import com.sumerge.survey.request.UpdateFormRequest;
import com.sumerge.survey.service.SurveyFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/survey-form")
public class SurveyFormController {
    @Autowired
    private SurveyFormService surveyFormService;

    @GetMapping("/{userId}")
    public List<SurveyForm> getUserSurveyForms(@PathVariable String userId) {
        return surveyFormService.getUserSurveyForms(userId);
    }

    @PostMapping("/record-section-state")
    public ResponseEntity<String> recordSectionState(@RequestBody SectionStateRequest request) {
        surveyFormService.recordSectionState(request.getFormId(), request.getSectionStates());
        return ResponseEntity.ok("Section state recorded successfully.");
    }

    @PostMapping("/create-form")
    public ResponseEntity<String> createForm(@RequestBody CreateFormRequest createFormRequest) {
        surveyFormService.createNewForm(
                createFormRequest.getFormId(),
                createFormRequest.getSectionStates()
        );
        return ResponseEntity.ok("Form created successfully.");
    }

    @PutMapping("/update-form")
    public ResponseEntity<String> updateForm(@RequestBody UpdateFormRequest updateFormRequest) {
        surveyFormService.updateForm(
                updateFormRequest.getFormId(),
                updateFormRequest.getSectionStates()
        );
        return ResponseEntity.ok("Form updated successfully.");
    }
    @GetMapping("/last-submit-timestamp/{userId}")
    public ResponseEntity<LocalDateTime> getLastSubmitTimestamp(@PathVariable long formId) {
        LocalDateTime lastSubmitTimestamp = surveyFormService.getLastSubmitTimestamp(formId);
        return ResponseEntity.ok(lastSubmitTimestamp);
    }
}
