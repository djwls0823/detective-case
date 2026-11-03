package com.detectivecase.evidence;

import com.detectivecase.evidence.model.EvidenceGetListRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EvidenceMapper {

    // 존재하지 않는 세션 요청을 구분하기 위해 사용한다.
    boolean existsSessionById(@Param("sessionId") String sessionId);

    /*
     * 세션이 진행 중인 CASE에 속한 증거 목록을 조회한다.
     *
     * caseId를 직접 받지 않으므로 다른 사건의 증거를
     * 임의로 조회할 수 없다.
     */
    List<EvidenceGetListRes> findAllBySessionId(
            @Param("sessionId") String sessionId
    );
}