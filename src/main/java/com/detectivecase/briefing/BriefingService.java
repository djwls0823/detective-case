package com.detectivecase.briefing;

import com.detectivecase.briefing.model.CaseBriefingGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BriefingService {

    private final BriefingMapper briefingMapper;

    /*
     * 사건 브리핑을 조회한다.
     *
     * Mapper 조회 결과가 null이라는 것은
     * 해당 sessionId가 game_sessions 테이블에 없다는 의미다.
     */
    public CaseBriefingGetRes getCaseBriefing(String sessionId) {
        CaseBriefingGetRes result = briefingMapper.findBySessionId(sessionId);

        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        return result;
    }
}
