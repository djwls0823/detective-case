package com.detectivecase.scene;

import com.detectivecase.scene.model.CaseSceneGetRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SceneService {

    private final SceneMapper sceneMapper;

    /*
     * 현장 화면에서 사용할 이미지 URL을 조회한다.
     *
     * 조회 결과가 null이면 해당 sessionId가 존재하지 않는 것이므로
     * 404 오류를 반환한다.
     */
    public CaseSceneGetRes getCaseScene(String sessionId) {
        CaseSceneGetRes result = sceneMapper.findBySessionId(sessionId);

        if (result == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        return result;
    }
}
