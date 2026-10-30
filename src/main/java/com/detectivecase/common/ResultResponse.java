package com.detectivecase.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "공통 API 응답")
public class ResultResponse<T> {

    @Schema(title = "결과 상태 코드", example = "201")
    private final String statusCode;

    @Schema(title = "결과 메시지", example = "익명 플레이어 생성 성공")
    private final String resultMsg;

    @Schema(title = "결과 내용")
    private final T resultData;
}