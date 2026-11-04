package com.detectivecase.hint;

import com.detectivecase.hint.model.HintGetRes;
import com.detectivecase.hint.model.HintHistoryGetRes;
import com.detectivecase.hint.model.HintSessionState;
import com.detectivecase.hint.model.HintUseRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HintService {

    private static final int MAX_HINT_COUNT = 3;
    private static final int HINT_SCORE_PENALTY = 5;

    private final HintMapper hintMapper;

    @Transactional
    public HintUseRes useHint(String sessionId) {
        HintSessionState sessionState =
                hintMapper.findSessionStateById(sessionId);

        if (sessionState == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        if (!"PLAYING".equals(sessionState.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 해결된 사건에서는 힌트를 사용할 수 없습니다."
            );
        }

        HintUseRes result = hintMapper.findNextUnusedHint(sessionId);

        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "더 이상 사용할 수 있는 힌트가 없습니다."
            );
        }

        hintMapper.insertHintHistory(sessionId, result.getHintId());
        hintMapper.useHint(sessionId);

        int hintCount = sessionState.getHintCount() + 1;

        result.setHintCount(hintCount);
        result.setRemainingHintCount(MAX_HINT_COUNT - hintCount);
        result.setScore(sessionState.getScore() - HINT_SCORE_PENALTY);

        return result;
    }

    // 사용한 힌트 기록과 현재 점수를 함께 조회한다.
    public HintHistoryGetRes getHintHistory(String sessionId) {
        HintSessionState sessionState =
                hintMapper.findSessionStateById(sessionId);

        if (sessionState == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        List<HintGetRes> hints = hintMapper.findUsedHints(sessionId);

        return HintHistoryGetRes.builder()
                .hints(hints)
                .hintCount(sessionState.getHintCount())
                .remainingHintCount(
                        Math.max(0, MAX_HINT_COUNT - sessionState.getHintCount())
                )
                .score(sessionState.getScore())
                .build();
    }
}