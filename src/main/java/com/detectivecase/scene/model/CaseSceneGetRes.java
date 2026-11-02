package com.detectivecase.scene.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CaseSceneGetRes {

    // 현재 진행 중인 사건 번호
    private Long caseId;

    // 화면 상단 등에 표시할 사건 제목
    private String caseTitle;

    /*
     * 현장 사진 주소다.
     *
     * 예시:
     * /images/cases/case-01-scene.jpg
     *
     * 사진 파일을 아직 등록하지 않았다면 null이 반환될 수 있다.
     */
    private String sceneImageUrl;
}
