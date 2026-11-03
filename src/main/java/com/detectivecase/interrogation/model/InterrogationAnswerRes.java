package com.detectivecase.interrogation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterrogationAnswerRes {

    // 선택한 질문 ID
    private Long questionId;

    // 화면에 표시할 질문 문장
    private String questionText;

    // 용의자가 해당 질문에 답한 진술 내용
    private String answerText;

    /*
     * 이번 요청으로 처음 심문 기록이 생성되었는지 여부다.
     *
     * true: 처음 선택한 질문
     * false: 이전에 이미 확인한 질문
     */
    private Boolean isNewQuestion;
}
