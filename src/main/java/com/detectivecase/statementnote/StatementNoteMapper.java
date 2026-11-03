package com.detectivecase.statementnote;

import com.detectivecase.statementnote.model.StatementNoteRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatementNoteMapper {

    // 세션 존재 여부를 먼저 확인한다.
    boolean existsSessionById(@Param("sessionId") String sessionId);

    /*
     * 현재 CASE의 전체 질문 수와 해당 세션의 심문 기록 수를 비교한다.
     *
     * 모든 질문을 확인해야 true가 된다.
     */
    boolean isAllInterrogationsCompleted(
            @Param("sessionId") String sessionId
    );

    /*
     * 용의자, 기본 진술, 질문, 답변을 한 행씩 조회한다.
     *
     * 같은 용의자의 여러 질문은 여러 행으로 조회되며,
     * Service에서 용의자별로 묶는다.
     */
    List<StatementNoteRow> findAllBySessionId(
            @Param("sessionId") String sessionId
    );
}
