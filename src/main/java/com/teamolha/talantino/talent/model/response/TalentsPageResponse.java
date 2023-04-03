package com.teamolha.talantino.talent.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TalentsPageResponse {
    int totalAmount;
    List<TalentGeneralResponse> talents;
}
