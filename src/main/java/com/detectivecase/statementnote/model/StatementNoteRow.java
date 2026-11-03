package com.detectivecase.statementnote.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StatementNoteRow {

    /*
     * SQL 조인 결과 한 행을 잠시 담는 내부용 DTO다.
     *
     * 한 행에는 용의자 한 명의 질문·답변 하나가 들어간다.
     * Service에서 같은 용의자끼리 묶어 최종 응답을 만든다.
     */
    private Long suspectId;
    private String name;
    private String roleName;
    private String initialStatement;

    private Long questionId;
    private String questionText;
    private String answerText;
}
