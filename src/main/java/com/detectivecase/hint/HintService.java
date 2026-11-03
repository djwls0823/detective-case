package com.detectivecase.hint;

import com.detectivecase.hint.model.HintSessionState;
import com.detectivecase.hint.model.HintUseRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class HintService {

    private static final int MAX_HINT_COUNT = 3;
    private static final int HINT_SCORE_PENALTY = 5;

    private final HintMapper hintMapper;

    /*
     * 다음 힌트를 반환하고 힌트 사용 상태를 저장한다.
     *
     * 힌트 이력 저장과 점수 차감은 함께 성공해야 한다.
     * 하나라도 실패하면 둘 다 취소되도록 @Transactional을 사용한다.
     */
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

        // 해결이 끝난 사건은 추가 힌트를 사용할 수 없다.
        if (!"PLAYING".equals(sessionState.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 해결된 사건에서는 힌트를 사용할 수 없습니다."
            );
        }

        HintUseRes result = hintMapper.findNextUnusedHint(sessionId);

        // 사용하지 않은 힌트가 없으면 CASE의 힌트를 모두 사용한 상태다.
        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "더 이상 사용할 수 있는 힌트가 없습니다."
            );
        }

        // 어떤 힌트를 사용했는지 기록한다.
        hintMapper.insertHintHistory(sessionId, result.getHintId());

        // 점수 5점 차감 및 힌트 사용 횟수 증가
        hintMapper.useHint(sessionId);

        /*
         * DB를 다시 조회하지 않고, 힌트 사용 전 상태를 기준으로
         * 이번 요청 이후의 점수와 횟수를 계산한다.
         */
        int hintCount = sessionState.getHintCount() + 1;

        result.setHintCount(hintCount);
        result.setRemainingHintCount(MAX_HINT_COUNT - hintCount);
        result.setScore(sessionState.getScore() - HINT_SCORE_PENALTY);

        return result;
    }
}