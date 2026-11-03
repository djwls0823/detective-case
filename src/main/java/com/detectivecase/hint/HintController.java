package com.detectivecase.hint;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.hint.model.HintUseRes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game-sessions")
@RequiredArgsConstructor
public class HintController {

    private final HintService hintService;

    /*
     * 현장 메인 화면에서 힌트 버튼을 눌렀을 때 호출한다.
     *
     * 예시 요청:
     * POST /api/game-sessions/{sessionId}/hints
     */
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
}
