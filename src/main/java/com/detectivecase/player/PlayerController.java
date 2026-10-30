package com.detectivecase.player;

import com.detectivecase.common.ResultResponse;
import com.detectivecase.player.model.PlayerCreateRes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "익명 플레이어 생성")
    public ResultResponse<PlayerCreateRes> createPlayer() {
        PlayerCreateRes result = playerService.createPlayer();

        return ResultResponse.<PlayerCreateRes>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_CREATED))
                .resultMsg("익명 플레이어 생성 성공")
                .resultData(result)
                .build();
    }
}
