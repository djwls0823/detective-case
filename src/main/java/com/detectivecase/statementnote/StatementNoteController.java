package com.detectivecase.statementnote;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.statementnote.model.StatementNoteGetRes;
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
public class StatementNoteController {

    private final StatementNoteService statementNoteService;

    /*
     * 모든 심문을 완료한 뒤 진술 노트 화면에서 호출한다.
     *
     * 예시 요청:
     * GET /api/game-sessions/{sessionId}/statement-notes
     */
    @GetMapping("/{sessionId}/statement-notes")
    @Operation(summary = "용의자별 진술 노트 조회")
    public ResultResponse<List<StatementNoteGetRes>> getStatementNotes(
            @PathVariable String sessionId
    ) {
        List<StatementNoteGetRes> result =
                statementNoteService.getStatementNotes(sessionId);

        return ResultResponse.<List<StatementNoteGetRes>>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("진술 노트 조회 성공")
                .resultData(result)
                .build();
    }
}
