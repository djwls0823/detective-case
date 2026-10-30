package com.detectivecase.casefile;

import com.detectivecase.casefile.model.CaseGetListRes;
import com.detectivecase.common.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    @GetMapping
    @Operation(summary = "선택 가능한 CASE 조회")
    public ResultResponse<List<CaseGetListRes>> getCases(
            @RequestParam String playerId
    ) {
        List<CaseGetListRes> result = caseService.getCases(playerId);

        return ResultResponse.<List<CaseGetListRes>>builder()
                .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                .resultMsg("선택 가능한 CASE 조회 성공")
                .resultData(result)
                .build();
    }
}
