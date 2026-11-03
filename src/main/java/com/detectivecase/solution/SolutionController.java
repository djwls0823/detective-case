package com.detectivecase.solution;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.solution.model.SolutionSubmitReq;
import com.detectivecase.solution.model.SolutionSubmitRes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-sessions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    /*
     * 현장 메인 화면에서 '최종 답안 제출' 버튼을 눌렀을 때 호출한다.
     *
     * 예시 요청:
     * POST /api/game-sessions/{sessionId}/solution
     *
     * 오답은 서버 오류가 아닌 정상적인 게임 결과이므로
     * HTTP 200을 반환하고, 응답 데이터의 isCorrect로 정답 여부를 전달한다.
     */
    @PostMapping("/{sessionId}/solution")
    @Operation(summary = "최종 답안 제출 및 사건 해결 판정")
    public ResultResponse<SolutionSubmitRes> submitSolution(
            @PathVariable String sessionId,
            @Valid @RequestBody SolutionSubmitReq p
    ) {
        SolutionSubmitRes result = solutionService.submitSolution(
                sessionId,
                p
        );

        /*
         * 오답일 때 범인·증거·키워드 중 무엇이 틀렸는지는 알려 주지 않는다.
         * 플레이어가 다시 추리하도록 일반적인 메시지만 반환한다.
         */
        String resultMsg = result.getIsCorrect()
                ? "사건 해결 성공"
                : "정답이 아닙니다. 다시 추리해 보세요.";

        return ResultResponse.<SolutionSubmitRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg(resultMsg)
                .resultData(result)
                .build();
    }
}