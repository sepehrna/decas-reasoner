package com.decas.reasoner.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlanDto {

    private String deviceId;
    private String action;

}
