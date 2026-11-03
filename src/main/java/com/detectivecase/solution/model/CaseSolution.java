package com.detectivecase.solution.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CaseSolution {

    // 정답 범인 ID
    private Long culpritSuspectId;

    // 정답일 때만 공개할 사건의 진실 전문
    private String truthSummary;
}
