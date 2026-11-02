package com.detectivecase.scene;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.scene.model.CaseSceneGetRes;
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
public class SceneController {

    private final SceneService sceneService;

    /*
     * 사건 현장 메인 화면에 진입할 때 호출한다.
     *
     * 예시 요청:
     * GET /api/game-sessions/{sessionId}/scene
     */
    @GetMapping("/{sessionId}/scene")
    @Operation(summary = "사건 현장 이미지 조회")
    public ResultResponse<CaseSceneGetRes> getCaseScene(
            @PathVariable String sessionId
    ) {
        CaseSceneGetRes result = sceneService.getCaseScene(sessionId);

        return ResultResponse.<CaseSceneGetRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("사건 현장 이미지 조회 성공")
                .resultData(result)
                .build();
    }
}