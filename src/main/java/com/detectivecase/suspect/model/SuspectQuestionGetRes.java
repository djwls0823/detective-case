package com.detectivecase.suspect.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuspectQuestionGetRes {

    // INT-01에서 선택할 질문 ID
    private Long questionId;

    // 화면에 표시할 선택형 질문 문장
    private String questionText;

    // 현재 세션에서 이미 선택한 질문인지 여부
    private Boolean isAsked;
}
