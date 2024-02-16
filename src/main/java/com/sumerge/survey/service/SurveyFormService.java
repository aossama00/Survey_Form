package com.sumerge.survey.service;

// SurveyFormService.java
import com.sumerge.survey.enumeration.SectionState;
import com.sumerge.survey.entity.SurveyForm;
import com.sumerge.survey.exception.FormNotFoundException;
import com.sumerge.survey.mapper.SurveyFormMapper;
import com.sumerge.survey.repository.SurveyFormRepository;
import com.sumerge.survey.response.FormDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class SurveyFormService {

    @Autowired
    private SurveyFormRepository surveyFormRepository;

    @Autowired
    private SurveyFormMapper surveyFormMapper;


    public void createNewForm( Map<String, SectionState> sectionStates) {
        try {
            SurveyForm newForm = new SurveyForm();
            if (!sectionStates.isEmpty())
                sectionStates.forEach((section, state) -> {
                    setSectionState(newForm, section, state);
                });

            newForm.setLastSubmitTimestamp(LocalDateTime.now());
            surveyFormRepository.save(newForm);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public void updateForm(long formId, Map<String, SectionState> sectionStates) {
        Optional<SurveyForm> existingFormOptional = surveyFormRepository.findById(formId);

        if (existingFormOptional.isPresent()) {
            SurveyForm existingForm = existingFormOptional.get();

            sectionStates.forEach((section, state) ->
                setSectionState(existingForm, section, state)
            );

            existingForm.setLastSubmitTimestamp(LocalDateTime.now());
            surveyFormRepository.save(existingForm);
        }
        else {
            throw new FormNotFoundException("Form not found with ID: " + formId);
        }

    }

    private void setSectionState(SurveyForm form, String section, SectionState state) {
        switch (section) {
            case "environmental":
                form.setEnvironmentalSection(state);
                break;
            case "social":
                form.setSocialSection(state);
                break;
            case "governmental":
                form.setGovernmentalSection(state);
                break;
        }
    }
    private boolean formSubmitted(SurveyForm form) {
        return form.getEnvironmentalSection() == SectionState.COMPLETED &&
                form.getSocialSection() == SectionState.COMPLETED &&
                form.getGovernmentalSection() == SectionState.COMPLETED;
    }

    public LocalDateTime getLastSubmitTimestamp(long formId) {
            Optional<SurveyForm> formOptional = surveyFormRepository.findById(formId);
            if(formOptional.isPresent()) {
                SurveyForm form = formOptional.get();
                return form.getLastSubmitTimestamp();
            }
        return null;
    }

    public FormDetailsResponse getFormDetails(long formId) {
        return surveyFormRepository.findById(formId)
                .map(form -> {
                    FormDetailsResponse detailsResponse = new FormDetailsResponse();
                    detailsResponse.setId(form.getId());
                    detailsResponse.setDateAndTime(form.getLastSubmitTimestamp());
                    detailsResponse.setSocial_status(form.getSocialSection());
                    detailsResponse.setGovernmental_status(form.getGovernmentalSection());
                    detailsResponse.setEnvironmental_status(form.getEnvironmentalSection());
                    detailsResponse.setCompleted(this.formSubmitted(form));
                    return detailsResponse;
                })
                .orElse(null);
    }
}
