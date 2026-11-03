package com.detectivecase.suspect.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SuspectGetRes {

    // 현재 상세 조회 중인 용의자 ID
    private Long suspectId;

    // 용의자 이름
    private String name;

    // 피해자와의 관계 또는 직업
    private String roleName;

    // 심문 화면에 처음 표시할 기본 진술
    private String initialStatement;

    /*
     * 해당 용의자에게 선택할 수 있는 질문 목록이다.
     *
     * 용의자 정보와 질문 목록은 각각 별도의 SQL로 조회한 뒤
     * Service에서 이 필드에 넣는다.
     */
    private List<SuspectQuestionGetRes> questions;
}
