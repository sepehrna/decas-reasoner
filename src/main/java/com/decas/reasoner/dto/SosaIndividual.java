package com.decas.reasoner.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SosaIndividual {

    private String name;
    private String label;
    private String definition;
    private String comment;

}
