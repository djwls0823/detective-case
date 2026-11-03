package com.detectivecase.hint.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HintUseRes {

    // 이번에 반환한 힌트 ID
    private Long hintId;

    // CASE 안에서의 힌트 순서
    private Integer hintOrder;

    // 플레이어에게 보여 줄 힌트 문장
    private String content;

    // 이번 힌트를 포함한 총 사용 횟수
    private Integer hintCount;

    // 시나리오당 최대 3회 중 남은 힌트 횟수
    private Integer remainingHintCount;

    // 힌트 사용 후 5점이 차감된 현재 점수
    private Integer score;
}
