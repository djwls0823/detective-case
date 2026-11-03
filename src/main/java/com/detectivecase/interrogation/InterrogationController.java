package com.detectivecase.interrogation;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.interrogation.model.InterrogationAnswerRes;
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
public class InterrogationController {

    private final InterrogationService interrogationService;

    /*
     * 심문 화면에서 질문 버튼을 눌렀을 때 호출한다.
     *
     * 예시 요청:
     * POST /api/game-sessions/{sessionId}/questions/{questionId}
     *
     * 요청 본문은 필요 없다.
     * 선택한 questionId 자체가 심문할 질문을 의미한다.
     */
    @PostMapping("/{sessionId}/questions/{questionId}")
    @Operation(summary = "용의자 질문 및 답변 조회")
    public ResultResponse<InterrogationAnswerRes> askQuestion(
            @PathVariable String sessionId,
            @PathVariable Long questionId
    ) {
        InterrogationAnswerRes result = interrogationService.askQuestion(
                sessionId,
                questionId
        );

        return ResultResponse.<InterrogationAnswerRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("용의자 답변 조회 성공")
                .resultData(result)
                .build();
    }
}
