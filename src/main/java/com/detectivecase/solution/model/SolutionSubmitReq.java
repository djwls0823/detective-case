package com.detectivecase.solution.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolutionSubmitReq {

    // 플레이어가 범인으로 선택한 용의자 ID
    @NotNull
    @Positive
    private Long suspectId;

    // 플레이어가 핵심 증거로 선택한 증거 ID
    @NotNull
    @Positive
    private Long evidenceId;

    // 플레이어가 작성한 사건의 진실
    @NotBlank
    private String truth;
}
