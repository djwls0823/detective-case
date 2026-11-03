package com.detectivecase.suspect;

import com.detectivecase.suspect.model.SuspectGetListRes;
import com.detectivecase.suspect.model.SuspectGetRes;
import com.detectivecase.suspect.model.SuspectQuestionGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuspectService {

    private final SuspectMapper suspectMapper;

    /*
     * 용의자 진술 버튼을 눌렀을 때 호출한다.
     *
     * 세션이 존재하지 않을 때 빈 목록을 반환하지 않고,
     * 잘못된 요청임을 명확히 알리는 404 오류를 반환한다.
     */
    public List<SuspectGetListRes> getSuspects(String sessionId) {
        if (!suspectMapper.existsSessionById(sessionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        return suspectMapper.findAllBySessionId(sessionId);
    }

    /*
     * 용의자 한 명의 기본 진술과 질문 목록을 조회한다.
     *
     * 1. 세션 존재 여부 확인
     * 2. 요청한 용의자가 해당 세션의 CASE에 속하는지 확인
     * 3. 질문 목록과 각 질문의 확인 여부를 조합
     */
    public SuspectGetRes getSuspect(
            String sessionId,
            Long suspectId
    ) {
        if (!suspectMapper.existsSessionById(sessionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        SuspectGetRes result = suspectMapper.findBySessionIdAndSuspectId(
                sessionId,
                suspectId
        );

        // 다른 CASE의 용의자 ID를 요청한 경우도 여기에서 막는다.
        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "현재 CASE에 존재하지 않는 용의자입니다."
            );
        }

        List<SuspectQuestionGetRes> questions =
                suspectMapper.findQuestionsBySessionIdAndSuspectId(
                        sessionId,
                        suspectId
                );

        // 기본 진술 정보와 질문 목록을 하나의 응답 객체로 조합한다.
        result.setQuestions(questions);

        return result;
    }
}
