package com.detectivecase.briefing;

import com.detectivecase.briefing.model.CaseBriefingGetRes;
import com.detectivecase.common.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game-sessions")
@RequiredArgsConstructor
public class BriefingController {

    private final BriefingService briefingService;

    /*
     * 사건 브리핑 화면에 진입했을 때 호출한다.
     *
     * 예시 요청:
     * GET /api/game-sessions/{sessionId}/briefing
     */
    @GetMapping("/{sessionId}/briefing")
    @Operation(summary = "사건 브리핑 조회")
    public ResultResponse<CaseBriefingGetRes> getCaseBriefing(
            @PathVariable String sessionId
    ) {
        CaseBriefingGetRes result = briefingService.getCaseBriefing(sessionId);

        return ResultResponse.<CaseBriefingGetRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("사건 브리핑 조회 성공")
                .resultData(result)
                .build();
    }
}
