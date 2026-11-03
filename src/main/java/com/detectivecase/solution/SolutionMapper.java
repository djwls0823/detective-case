package com.detectivecase.solution;

import com.detectivecase.solution.model.CaseSolution;
import com.detectivecase.solution.model.SolutionKeywordRow;
import com.detectivecase.solution.model.SolutionSessionState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SolutionMapper {

    // 세션의 현재 CASE, 상태, 점수를 조회한다.
    SolutionSessionState findSessionStateById(
            @Param("sessionId") String sessionId
    );

    // 선택한 용의자가 현재 CASE에 속하는지 검증한다.
    boolean existsSuspectInCase(
            @Param("caseId") Long caseId,
            @Param("suspectId") Long suspectId
    );

    // 선택한 증거가 현재 CASE에 속하는지 검증한다.
    boolean existsEvidenceInCase(
            @Param("caseId") Long caseId,
            @Param("evidenceId") Long evidenceId
    );

    // CASE의 정답 범인과 사건 진실 전문을 조회한다.
    CaseSolution findCaseSolutionByCaseId(
            @Param("caseId") Long caseId
    );

    /*
     * 선택한 증거가 정답 증거 목록에 포함되는지 확인한다.
     *
     * CASE 01은 음식, 요리책, 전자레인지 중
     * 하나를 선택하면 정답 증거로 인정한다.
     */
    boolean isCorrectEvidence(
            @Param("caseId") Long caseId,
            @Param("evidenceId") Long evidenceId
    );

    // 추리 내용의 키워드 판정에 사용할 키워드 목록을 조회한다.
    List<SolutionKeywordRow> findKeywordRowsByCaseId(
            @Param("caseId") Long caseId
    );

    /*
     * 정답 시 세션을 해결 상태로 변경하고 최종 제출 내용을 저장한다.
     */
    int markSolved(
            @Param("sessionId") String sessionId,
            @Param("suspectId") Long suspectId,
            @Param("evidenceId") Long evidenceId,
            @Param("truth") String truth,
            @Param("grade") String grade
    );

    /*
     * 오답 시 점수를 10점 차감하고 오답 횟수를 증가한다.
     *
     * GREATEST를 사용해 점수가 음수가 되는 것을 막는다.
     */
    int applyWrongAnswerPenalty(
            @Param("sessionId") String sessionId
    );
}
