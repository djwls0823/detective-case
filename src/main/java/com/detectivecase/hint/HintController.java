package com.detectivecase.hint;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.hint.model.HintHistoryGetRes;
import com.detectivecase.hint.model.HintUseRes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game-sessions")
@RequiredArgsConstructor
public class HintController {

    private final HintService hintService;

    @PostMapping("/{sessionId}/hints")
    @Operation(summary = "다음 힌트 사용")
    public ResultResponse<HintUseRes> useHint(
            @PathVariable String sessionId
    ) {
        HintUseRes result = hintService.useHint(sessionId);

        return ResultResponse.<HintUseRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("힌트 사용 성공")
                .resultData(result)
                .build();
    }

    @GetMapping("/{sessionId}/hints")
    @Operation(summary = "사용한 힌트 기록 조회")
    public ResultResponse<HintHistoryGetRes> getHintHistory(
            @PathVariable String sessionId
    ) {
        HintHistoryGetRes result = hintService.getHintHistory(sessionId);

        return ResultResponse.<HintHistoryGetRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("사용한 힌트 기록 조회 성공")
                .resultData(result)
                .build();
    }
}
