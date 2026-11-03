package com.detectivecase.hint.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HintSessionState {

    // PLAYING 또는 SOLVED
    private String status;

    // 힌트 사용 전 현재 점수
    private Integer score;

    // 힌트 사용 전 사용 횟수
    private Integer hintCount;
}
