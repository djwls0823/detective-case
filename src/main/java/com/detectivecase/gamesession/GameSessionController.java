package com.detectivecase.gamesession;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.gamesession.model.GameSessionCreateReq;
import com.detectivecase.gamesession.model.GameSessionCreateRes;
import com.detectivecase.gamesession.model.GameSessionGetRes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-sessions")
@RequiredArgsConstructor
public class GameSessionController {

    private final GameSessionService gameSessionService;

    /*
     * CASE 선택 후 새 게임을 시작할 때 호출한다.
     *
     * @Valid:
     * - playerId가 비어 있지 않은지
     * - caseId가 null, 0, 음수가 아닌지
     * 를 컨트롤러 진입 전에 검증한다.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "게임 세션 생성")
    public ResultResponse<GameSessionCreateRes> createGameSession(
            @Valid @RequestBody GameSessionCreateReq p
    ) {
        GameSessionCreateRes result = gameSessionService.createGameSession(p);

        return ResultResponse.<GameSessionCreateRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_CREATED))
                .resultMsg("게임 세션 생성 성공")
                .resultData(result)
                .build();
    }

    /*
     * 이어서 하기 버튼을 눌렀을 때 호출한다.
     *
     * 예시:
     * GET /api/game-sessions/8d3122aa-9acd-4eab-87e1-8f4b631dcf6c
     */
    @GetMapping("/{sessionId}")
    @Operation(summary = "게임 세션 조회")
    public ResultResponse<GameSessionGetRes> getGameSession(
            @PathVariable String sessionId
    ) {
        GameSessionGetRes result = gameSessionService.getGameSession(sessionId);

        return ResultResponse.<GameSessionGetRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("게임 세션 조회 성공")
                .resultData(result)
                .build();
    }

    /*
     * 새로 시작하기를 선택했을 때 기존 게임 진행 상태를 삭제한다.
     *
     * 삭제가 성공하면 응답 본문 없이 HTTP 204 No Content를 반환한다.
     */
    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "게임 세션 삭제")
    public void deleteGameSession(
            @PathVariable String sessionId
    ) {
        gameSessionService.deleteGameSession(sessionId);
    }
}