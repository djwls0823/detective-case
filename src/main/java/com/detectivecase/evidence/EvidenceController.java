package com.detectivecase.evidence;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.evidence.model.EvidenceGetListRes;
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
public class EvidenceController {

    private final EvidenceService evidenceService;

    /*
     * 현장 메인 화면의 '증거 노트' 버튼을 눌렀을 때 호출한다.
     *
     * 예시 요청:
     * GET /api/game-sessions/{sessionId}/evidences
     */
    @GetMapping("/{sessionId}/evidences")
    @Operation(summary = "증거 노트 조회")
    public ResultResponse<List<EvidenceGetListRes>> getEvidences(
            @PathVariable String sessionId
    ) {
        List<EvidenceGetListRes> result =
                evidenceService.getEvidences(sessionId);

        return ResultResponse.<List<EvidenceGetListRes>>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("증거 노트 조회 성공")
                .resultData(result)
                .build();
    }
}
