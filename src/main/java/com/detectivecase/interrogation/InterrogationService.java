package com.detectivecase.interrogation;

import com.detectivecase.interrogation.model.InterrogationAnswerRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class InterrogationService {

    private final InterrogationMapper interrogationMapper;

    /*
     * 질문을 선택하고 해당 답변을 반환한다.
     *
     * 질문이 현재 세션의 CASE에 속하는지 먼저 조회로 검증한 뒤,
     * 심문 이력을 저장한다.
     */
    public InterrogationAnswerRes askQuestion(
            String sessionId,
            Long questionId
    ) {
        if (!interrogationMapper.existsSessionById(sessionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        InterrogationAnswerRes result =
                interrogationMapper.findAnswerBySessionIdAndQuestionId(
                        sessionId,
                        questionId
                );

        // 존재하지 않는 질문이거나 다른 CASE의 질문인 경우다.
        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "현재 CASE에 존재하지 않는 질문입니다."
            );
        }

        /*
         * INSERT IGNORE의 실행 결과를 이용한다.
         *
         * 1이면 새로운 질문 기록이 생성된 것이고,
         * 0이면 플레이어가 이전에 이미 확인했던 질문이다.
         */
        int insertedRowCount = interrogationMapper.insertHistory(
                sessionId,
                questionId
        );

        result.setIsNewQuestion(insertedRowCount == 1);

        return result;
    }
}
