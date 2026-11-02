package com.detectivecase.gamesession;

import com.detectivecase.gamesession.model.GameSessionCreateReq;
import com.detectivecase.gamesession.model.GameSessionCreateRes;
import com.detectivecase.gamesession.model.GameSessionGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameSessionMapper gameSessionMapper;

    public GameSessionCreateRes createGameSession(GameSessionCreateReq p) {

        // 존재하지 않는 UUID로 세션을 만드는 요청을 막는다.
        if (!gameSessionMapper.existsPlayerById(p.getPlayerId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 플레이어입니다."
            );
        }

        // 존재하지 않는 CASE 번호를 요청한 경우다.
        if (!gameSessionMapper.existsCaseById(p.getCaseId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 CASE입니다."
            );
        }

        // CASE 2 이후에는 이전 사건 해결 전까지 접근할 수 없다.
        if (!gameSessionMapper.isCaseUnlocked(p.getPlayerId(), p.getCaseId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "아직 선택할 수 없는 CASE입니다."
            );
        }

        // 한 플레이어가 같은 CASE를 중복 시작하지 못하게 한다.
        // 기존 게임은 이후 '이어서 하기' 기능으로 불러온다.
        if (gameSessionMapper.existsByPlayerIdAndCaseId(
                p.getPlayerId(),
                p.getCaseId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 해당 CASE의 게임 세션이 존재합니다."
            );
        }

        // 로그인 없이 플레이어를 구분하기 위해 세션마다 UUID를 생성한다.
        String sessionId = UUID.randomUUID().toString();

        // DB에 기본 게임 상태(PLAYING, 100점 등)를 저장한다.
        gameSessionMapper.insert(
                sessionId,
                p.getPlayerId(),
                p.getCaseId()
        );

        // 생성 직후의 세션 정보를 API 응답으로 반환한다.
        return gameSessionMapper.findById(sessionId);
    }

    /*
     * localStorage에 저장된 sessionId로 기존 게임 진행 상태를 불러온다.
     *
     * 프론트엔드는 이 응답의 caseId를 이용해
     * 사건 브리핑, 현장 화면, 진술, 증거 API를 이어서 호출하게 된다.
     */
    public GameSessionGetRes getGameSession(String sessionId) {
        GameSessionGetRes result = gameSessionMapper.findDetailById(sessionId);

        // UUID가 없거나 삭제된 세션이라면 이어서 할 게임이 없는 상태다.
        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        return result;
    }

    /*
     * 현재 진행 중인 게임을 처음부터 다시 시작하기 위해 세션을 삭제한다.
     *
     * 세션 삭제 과정 중 하나라도 실패하면 기존 데이터가 일부만 삭제되면 안 된다.
     * 따라서 @Transactional로 모든 삭제 작업을 하나의 작업 단위로 묶는다.
     */
    @Transactional
    public void deleteGameSession(String sessionId) {
        if (!gameSessionMapper.existsById(sessionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        // 외래 키를 참조하는 자식 테이블부터 삭제한다.
        gameSessionMapper.deleteInterrogationHistoryBySessionId(sessionId);
        gameSessionMapper.deleteHintHistoryBySessionId(sessionId);

        // 하위 기록 삭제가 완료된 경우에만 부모 세션을 삭제한다.
        gameSessionMapper.deleteById(sessionId);
    }
}