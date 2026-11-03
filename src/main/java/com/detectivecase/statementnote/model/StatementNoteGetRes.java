package com.detectivecase.statementnote.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StatementNoteGetRes {

    // 용의자 정보
    private Long suspectId;
    private String name;
    private String roleName;

    // 심문 화면에 처음 표시했던 기본 진술
    private String initialStatement;

    /*
     * 질문과 답변 전문 목록이다.
     *
     * 선언과 동시에 빈 ArrayList로 생성해,
     * Service에서 add() 할 때 null 오류가 나지 않게 한다.
     */
    private List<StatementNoteItemRes> statements = new ArrayList<>();
}
