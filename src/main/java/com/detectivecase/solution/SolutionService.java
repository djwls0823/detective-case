package com.detectivecase.solution;

import com.detectivecase.solution.model.CaseSolution;
import com.detectivecase.solution.model.SolutionKeywordRow;
import com.detectivecase.solution.model.SolutionSessionState;
import com.detectivecase.solution.model.SolutionSubmitReq;
import com.detectivecase.solution.model.SolutionSubmitRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private static final int WRONG_ANSWER_SCORE_PENALTY = 10;

    private final SolutionMapper solutionMapper;

    /*
     * 최종 답안을 판정한다.
     *
     * 범인, 증거, 키워드 세 조건을 모두 만족해야만 정답이다.
     * 오답 시 어떤 조건이 틀렸는지는 응답에 포함하지 않는다.
     */
    @Transactional
    public SolutionSubmitRes submitSolution(
            String sessionId,
            SolutionSubmitReq p
    ) {
        SolutionSessionState sessionState =
                solutionMapper.findSessionStateById(sessionId);

        if (sessionState == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        // 해결된 사건을 다시 제출하는 요청은 막는다.
        if (!"PLAYING".equals(sessionState.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "이미 해결된 사건입니다."
            );
        }

        /*
         * 현재 CASE에 존재하지 않는 용의자·증거 ID는
         * 잘못된 요청이므로 답안 오답 처리와 구분한다.
         */
        if (!solutionMapper.existsSuspectInCase(
                sessionState.getCaseId(),
                p.getSuspectId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "현재 CASE에 존재하지 않는 용의자입니다."
            );
        }

        if (!solutionMapper.existsEvidenceInCase(
                sessionState.getCaseId(),
                p.getEvidenceId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "현재 CASE에 존재하지 않는 증거입니다."
            );
        }

        CaseSolution caseSolution =
                solutionMapper.findCaseSolutionByCaseId(
                        sessionState.getCaseId()
                );

        boolean isCorrectSuspect =
                caseSolution.getCulpritSuspectId().equals(p.getSuspectId());

        boolean isCorrectEvidence =
                solutionMapper.isCorrectEvidence(
                        sessionState.getCaseId(),
                        p.getEvidenceId()
                );

        boolean hasAllKeywordGroups = hasAllKeywordGroups(
                p.getTruth(),
                solutionMapper.findKeywordRowsByCaseId(
                        sessionState.getCaseId()
                )
        );

        boolean isCorrect = isCorrectSuspect
                && isCorrectEvidence
                && hasAllKeywordGroups;

        if (!isCorrect) {
            // 오답 원인은 숨기고 점수와 오답 횟수만 변경한다.
            solutionMapper.applyWrongAnswerPenalty(sessionId);

            return SolutionSubmitRes.builder()
                    .isCorrect(false)
                    .score(Math.max(
                            sessionState.getScore()
                                    - WRONG_ANSWER_SCORE_PENALTY,
                            0
                    ))
                    .grade(null)
                    .truthSummary(null)
                    .build();
        }

        String grade = calculateGrade(sessionState.getScore());

        // 정답일 때만 세션을 SOLVED로 변경하고 정답 제출 내용을 저장한다.
        solutionMapper.markSolved(
                sessionId,
                p.getSuspectId(),
                p.getEvidenceId(),
                p.getTruth(),
                grade
        );

        return SolutionSubmitRes.builder()
                .isCorrect(true)
                .score(sessionState.getScore())
                .grade(grade)
                .truthSummary(caseSolution.getTruthSummary())
                .build();
    }

    /*
     * 키워드 그룹마다 하나 이상의 키워드가 추리 내용에 포함되어야 한다.
     *
     * CASE 01 예시:
     * - 정전 그룹: 정전 또는 차단기 등
     * - 식재료 변질 그룹: 상한 또는 변질 등
     * - 샐러드 그룹: 샐러드 또는 생채소 등
     */
    private boolean hasAllKeywordGroups(
            String truth,
            List<SolutionKeywordRow> keywordRows
    ) {
        // 대소문자 차이 없이 비교하기 위해 모두 소문자로 변환한다.
        String normalizedTruth = truth.toLowerCase(Locale.ROOT);

        /*
         * SQL에서 조회한 행 데이터를 키워드 그룹별 문자열 목록으로 묶는다.
         *
         * 예시:
         * 1 → ["정전", "차단기", "두꺼비집"]
         * 2 → ["상한", "변질", "부패"]
         */
        Map<Long, List<String>> keywordGroups =
                keywordRows.stream()
                        .collect(Collectors.groupingBy(
                                SolutionKeywordRow::getKeywordGroupId,
                                Collectors.mapping(
                                        SolutionKeywordRow::getKeywordText,
                                        Collectors.toList()
                                )
                        ));

        // 키워드 그룹 설정이 없는 CASE는 정답 판정을 허용하지 않는다.
        if (keywordGroups.isEmpty()) {
            return false;
        }

        /*
         * 모든 키워드 그룹을 만족해야 한다.
         *
         * 각 그룹 안에서는 키워드 하나만 추리 내용에 포함되어도 통과한다.
         */
        return keywordGroups.values().stream()
                .allMatch(keywords ->
                        keywords.stream()
                                .map(keyword ->
                                        keyword.toLowerCase(Locale.ROOT)
                                )
                                .anyMatch(normalizedTruth::contains)
                );
    }

    /*
     * 점수는 힌트 사용 시 5점, 오답 시 10점씩 차감된다.
     * 따라서 실제 도달 가능한 점수 단위에 맞춰 등급을 계산한다.
     */
    private String calculateGrade(int score) {
        if (score >= 90) {
            return "S";
        }

        if (score >= 80) {
            return "A";
        }

        if (score >= 70) {
            return "B";
        }

        if (score >= 60) {
            return "C";
        }

        return "D";
    }
}
