package com.detectivecase.suspect;

import com.detectivecase.suspect.model.SuspectGetListRes;
import com.detectivecase.suspect.model.SuspectGetRes;
import com.detectivecase.suspect.model.SuspectQuestionGetRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SuspectMapper {

    // 요청에 사용한 게임 세션이 실제로 존재하는지 확인한다.
    boolean existsSessionById(@Param("sessionId") String sessionId);

    /*
     * 현재 세션의 CASE에 포함된 용의자 목록을 조회한다.
     *
     * 각 용의자에게 연결된 전체 질문 수와
     * 현재 세션에서 답변을 확인한 질문 수를 비교해 심문 완료 여부를 계산한다.
     */
    List<SuspectGetListRes> findAllBySessionId(
            @Param("sessionId") String sessionId
    );

    /*
     * 현재 세션의 CASE에 실제로 포함된 용의자인지 확인하면서
     * 용의자의 기본 정보를 조회한다.
     */
    SuspectGetRes findBySessionIdAndSuspectId(
            @Param("sessionId") String sessionId,
            @Param("suspectId") Long suspectId
    );

    /*
     * 해당 용의자에게 준비된 질문 목록을 가져온다.
     * interrogation_history와 비교해 이미 질문한 항목인지 함께 계산한다.
     */
    List<SuspectQuestionGetRes> findQuestionsBySessionIdAndSuspectId(
            @Param("sessionId") String sessionId,
            @Param("suspectId") Long suspectId
    );
}