package com.detectivecase.gamesession.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GameSessionGetRes {

    // 이어서 하기와 이후 API 호출에 사용할 게임 세션 UUID
    private String sessionId;

    // 현재 플레이 중인 사건 번호
    private Long caseId;

    // CASE 선택 화면이나 상단 정보에 표시할 사건 제목
    private String caseTitle;

    // PLAYING 또는 SOLVED
    private String status;

    // 최초 100점에서 힌트와 오답에 따라 차감된 현재 점수
    private Integer score;

    // 사용한 힌트 횟수. 시나리오당 최대 3회다.
    private Integer hintCount;

    // 최종 답안 오답 횟수
    private Integer wrongAnswerCount;

    // 사건 해결 후 확정되는 등급. 아직 해결 전이면 null이다.
    private String grade;

    // 세션 생성 및 마지막 진행 상태 변경 시각
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
