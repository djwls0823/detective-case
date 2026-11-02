package com.detectivecase.gamesession.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameSessionCreateReq {

    @NotBlank
    private String playerId;

    @NotNull
    @Positive
    private Long caseId;
}
