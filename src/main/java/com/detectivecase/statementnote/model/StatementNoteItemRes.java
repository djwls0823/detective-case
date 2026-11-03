package com.detectivecase.statementnote.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatementNoteItemRes {

    // 질문 식별값
    private Long questionId;

    // 플레이어가 선택했던 질문
    private String questionText;

    // 해당 질문에 대한 용의자의 답변
    private String answerText;
}
