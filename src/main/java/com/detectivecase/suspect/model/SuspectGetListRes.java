package com.detectivecase.suspect.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuspectGetListRes {

    // 용의자 상세 조회와 질문 API에서 사용할 용의자 ID
    private Long suspectId;

    // 예: 오만수
    private String name;

    // 예: 별장 관리인
    private String roleName;

    /*
     * 해당 용의자에게 준비된 모든 질문을 완료했는지 여부다.
     *
     * Boolean 객체형을 사용해 MyBatis가
     * isInterrogationCompleted 필드에 안정적으로 값을 주입하게 한다.
     */
    private Boolean isInterrogationCompleted;
}
