package com.detectivecase.hint;

import com.detectivecase.hint.model.HintGetRes;
import com.detectivecase.hint.model.HintSessionState;
import com.detectivecase.hint.model.HintUseRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HintMapper {

    // 세션의 상태, 점수, 기존 힌트 사용 횟수를 조회한다.
    HintSessionState findSessionStateById(
            @Param("sessionId") String sessionId
    );

    // 아직 사용하지 않은 다음 힌트 하나를 조회한다.
    HintUseRes findNextUnusedHint(
            @Param("sessionId") String sessionId
    );

    // 현재 세션에서 이미 사용한 힌트 목록을 조회한다.
    List<HintGetRes> findUsedHints(
            @Param("sessionId") String sessionId
    );

    // 해당 힌트를 사용한 사실을 기록한다.
    int insertHintHistory(
            @Param("sessionId") String sessionId,
            @Param("hintId") Long hintId
    );

    // 점수 5점 차감 및 힌트 사용 횟수 증가
    int useHint(@Param("sessionId") String sessionId);
}
