package com.detectivecase.scene;

import com.detectivecase.scene.model.CaseSceneGetRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SceneMapper {

    /*
     * 세션 ID를 기준으로 현재 플레이 중인 CASE의 현장 이미지 주소를 조회한다.
     *
     * CASE ID를 직접 받지 않아 다른 사건 현장을 임의로 조회할 수 없다.
     */
    CaseSceneGetRes findBySessionId(
            @Param("sessionId") String sessionId
    );
}
