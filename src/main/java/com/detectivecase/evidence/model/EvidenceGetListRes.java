package com.detectivecase.evidence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EvidenceGetListRes {

    // 최종 답안 제출 화면에서 선택할 증거 ID
    private Long evidenceId;

    // 예: 백도윤의 식탁 위 음식
    private String name;

    /*
     * 증거 노트에 표시할 관찰 가능한 사실이다.
     *
     * 범인을 지목하거나 증거의 의미를 해석하는 내용은 넣지 않는다.
     */
    private String description;

    /*
     * 증거 이미지 주소다.
     *
     * 이미지가 아직 준비되지 않았다면 null일 수 있으며,
     * 프론트엔드는 기본 이미지를 표시하거나 이미지 영역을 숨길 수 있다.
     */
    private String imageUrl;
}
