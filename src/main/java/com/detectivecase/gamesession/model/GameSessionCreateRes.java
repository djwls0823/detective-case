package com.detectivecase.gamesession.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GameSessionCreateRes {

    private String sessionId;
    private Long caseId;
    private String status;
    private Integer score;
    private Integer hintCount;
    private Integer wrongAnswerCount;
}