package com.detectivecase.solution.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SolutionSubmitRes {

    // 최종 정답 여부
    private Boolean isCorrect;

    // 제출 처리 후 현재 점수
    private Integer score;

    /*
     * 정답일 때만 확정되는 등급이다.
     *
     * 오답이면 아직 사건 해결 전이므로 null을 반환한다.
     */
    private String grade;

    /*
     * 정답일 때만 반환하는 사건의 진실 전문이다.
     *
     * 오답 시에는 null이므로 정답이 노출되지 않는다.
     */
    private String truthSummary;
}
