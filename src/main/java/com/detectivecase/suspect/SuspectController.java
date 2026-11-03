package com.detectivecase.suspect;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.suspect.model.SuspectGetListRes;
import com.detectivecase.suspect.model.SuspectGetRes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/game-sessions")
@RequiredArgsConstructor
public class SuspectController {

    private final SuspectService suspectService;

    /*
     * 현장 메인 화면의 '용의자 진술' 버튼을 눌렀을 때 호출한다.
     *
     * 예시 요청:
     * GET /api/game-sessions/{sessionId}/suspects
     */
    @GetMapping("/{sessionId}/suspects")
    @Operation(summary = "용의자 목록 조회")
    public ResultResponse<List<SuspectGetListRes>> getSuspects(
            @PathVariable String sessionId
    ) {
        List<SuspectGetListRes> result = suspectService.getSuspects(sessionId);

        return ResultResponse.<List<SuspectGetListRes>>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("용의자 목록 조회 성공")
                .resultData(result)
                .build();
    }

    /*
     * 용의자 목록에서 한 명을 선택했을 때 호출한다.
     *
     * 예시 요청:
     * GET /api/game-sessions/{sessionId}/suspects/{suspectId}
     */
    @GetMapping("/{sessionId}/suspects/{suspectId}")
    @Operation(summary = "용의자 기본 진술 및 질문 조회")
    public ResultResponse<SuspectGetRes> getSuspect(
            @PathVariable String sessionId,
            @PathVariable Long suspectId
    ) {
        SuspectGetRes result = suspectService.getSuspect(
                sessionId,
                suspectId
        );

        return ResultResponse.<SuspectGetRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("용의자 기본 진술 조회 성공")
                .resultData(result)
                .build();
    }
}
