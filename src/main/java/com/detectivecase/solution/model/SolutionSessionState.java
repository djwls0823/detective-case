package com.detectivecase.solution.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolutionSessionState {

    // 현재 플레이 중인 CASE 번호
    private Long caseId;

    // PLAYING 또는 SOLVED
    private String status;

    // 답안 제출 전 현재 점수
    private Integer score;
}