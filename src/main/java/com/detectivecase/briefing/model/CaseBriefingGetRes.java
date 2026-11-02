package com.detectivecase.briefing.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CaseBriefingGetRes {

    // 현재 세션이 진행 중인 CASE 번호
    private Long caseId;

    // 예: CASE 01 — 식탁 위의 거짓말
    private String title;

    // 플레이어에게 쪽지 형태로 보여 줄 사건 설명
    private String briefing;

    // 피해자 정보
    private String victimName;

    // 사건 발생 장소
    private String location;

    // 사망 추정 시각
    private String estimatedDeathTime;

    // 사인
    private String causeOfDeath;
}
