package com.detectivecase.gamesession;

import com.detectivecase.gamesession.model.GameSessionCreateRes;
import com.detectivecase.gamesession.model.GameSessionGetRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GameSessionMapper {

    // 요청한 플레이어 UUID가 실제로 등록된 플레이어인지 확인한다.
    boolean existsPlayerById(@Param("playerId") String playerId);

    // 존재하지 않는 CASE를 선택하는 요청을 방지한다.
    boolean existsCaseById(@Param("caseId") Long caseId);

    // CASE 1은 기본 공개, 이후 CASE는 이전 CASE 해결 여부를 기준으로 확인한다.
    boolean isCaseUnlocked(
            @Param("playerId") String playerId,
            @Param("caseId") Long caseId
    );

    // 한 플레이어는 하나의 CASE에 하나의 세션만 가질 수 있다.
    // 중복 생성 전 서비스에서 먼저 확인하기 위해 사용한다.
    boolean existsByPlayerIdAndCaseId(
            @Param("playerId") String playerId,
            @Param("caseId") Long caseId
    );

    // 새 게임 세션을 생성한다.
    // 세션 UUID는 서비스에서 만들고, 초기 점수와 상태는 SQL에서 설정한다.
    int insert(
            @Param("sessionId") String sessionId,
            @Param("playerId") String playerId,
            @Param("caseId") Long caseId
    );

    // INSERT 후 실제로 저장된 게임 세션 정보를 응답용 DTO로 조회한다.
    GameSessionCreateRes findById(@Param("sessionId") String sessionId);

    // sessionId로 이어서 할 게임의 현재 상태를 조회한다.
    GameSessionGetRes findDetailById(@Param("sessionId") String sessionId);

    // 삭제할 게임 세션이 존재하는지 확인한다.
    boolean existsById(@Param("sessionId") String sessionId);

    // 세션이 남긴 심문 기록을 먼저 삭제한다.
    int deleteInterrogationHistoryBySessionId(@Param("sessionId") String sessionId);

    // 세션이 남긴 힌트 사용 기록을 먼저 삭제한다.
    int deleteHintHistoryBySessionId(@Param("sessionId") String sessionId);

    // 모든 하위 기록 삭제 후 게임 세션 자체를 삭제한다.
    int deleteById(@Param("sessionId") String sessionId);
}
