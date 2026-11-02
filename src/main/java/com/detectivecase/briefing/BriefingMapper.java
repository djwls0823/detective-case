package com.detectivecase.briefing;

import com.detectivecase.briefing.model.CaseBriefingGetRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BriefingMapper {

    /*
     * 세션 ID를 기준으로 현재 진행 중인 CASE의 브리핑 정보를 조회한다.
     *
     * game_sessions에 저장된 case_id를 사용하므로,
     * 클라이언트가 다른 CASE 번호를 임의로 요청할 수 없다.
     */
    CaseBriefingGetRes findBySessionId(
            @Param("sessionId") String sessionId
    );
}
