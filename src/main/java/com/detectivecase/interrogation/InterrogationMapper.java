package com.detectivecase.interrogation;

import com.detectivecase.interrogation.model.InterrogationAnswerRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InterrogationMapper {

    // 존재하지 않는 세션 요청을 먼저 구분한다.
    boolean existsSessionById(@Param("sessionId") String sessionId);

    /*
     * 현재 세션의 CASE에 속한 질문인지 검증하면서
     * 질문 내용과 연결된 답변을 함께 조회한다.
     */
    InterrogationAnswerRes findAnswerBySessionIdAndQuestionId(
            @Param("sessionId") String sessionId,
            @Param("questionId") Long questionId
    );

    /*
     * 질문 선택 이력을 저장한다.
     *
     * 같은 sessionId와 questionId는 기본 키로 중복될 수 없다.
     * INSERT IGNORE를 사용해 이미 선택한 질문은 오류 없이 무시한다.
     */
    int insertHistory(
            @Param("sessionId") String sessionId,
            @Param("questionId") Long questionId
    );
}
