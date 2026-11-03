package com.detectivecase.hint;

import com.detectivecase.hint.model.HintSessionState;
import com.detectivecase.hint.model.HintUseRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HintMapper {

    // 세션의 상태, 점수, 기존 힌트 사용 횟수를 조회한다.
    HintSessionState findSessionStateById(
            @Param("sessionId") String sessionId
    );

    /*
     * 현재 세션이 아직 사용하지 않은 힌트 중
     * 가장 순서가 빠른 힌트 하나를 조회한다.
     */
    HintUseRes findNextUnusedHint(
            @Param("sessionId") String sessionId
    );

    // 해당 힌트를 사용한 사실을 기록한다.
    int insertHintHistory(
            @Param("sessionId") String sessionId,
            @Param("hintId") Long hintId
    );

    /*
     * 힌트 사용에 따른 상태를 반영한다.
     *
     * 점수는 5점 차감하고, hint_count는 1 증가한다.
     */
    int useHint(@Param("sessionId") String sessionId);
}
