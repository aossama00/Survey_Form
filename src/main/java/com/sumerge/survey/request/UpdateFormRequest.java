package com.sumerge.survey.request;

import com.sumerge.survey.enumeration.SectionState;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class UpdateFormRequest {
    private long formId;
    private Map<String, SectionState> sectionStates;
}
