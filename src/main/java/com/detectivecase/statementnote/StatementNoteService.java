package com.detectivecase.statementnote;

import com.detectivecase.statementnote.model.StatementNoteGetRes;
import com.detectivecase.statementnote.model.StatementNoteItemRes;
import com.detectivecase.statementnote.model.StatementNoteRow;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatementNoteService {

    private final StatementNoteMapper statementNoteMapper;

    /*
     * 모든 심문을 완료한 플레이어에게만 진술 노트를 반환한다.
     *
     * LinkedHashMap을 사용하면 SQL의 조회 순서인
     * 용의자 표시 순서를 그대로 유지할 수 있다.
     */
    public List<StatementNoteGetRes> getStatementNotes(String sessionId) {
        if (!statementNoteMapper.existsSessionById(sessionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        if (!statementNoteMapper.isAllInterrogationsCompleted(sessionId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "모든 용의자 심문을 완료한 후 진술 노트를 확인할 수 있습니다."
            );
        }

        List<StatementNoteRow> rows =
                statementNoteMapper.findAllBySessionId(sessionId);

        /*
         * SQL의 평면적인 행 데이터를 아래 형태로 변환한다.
         *
         * 용의자 1명
         * └─ 질문·답변 목록 여러 개
         */
        Map<Long, StatementNoteGetRes> noteMap = new LinkedHashMap<>();

        for (StatementNoteRow row : rows) {
            StatementNoteGetRes note = noteMap.get(row.getSuspectId());

            // 해당 용의자가 처음 등장한 행일 때 기본 정보를 생성한다.
            if (note == null) {
                note = new StatementNoteGetRes();
                note.setSuspectId(row.getSuspectId());
                note.setName(row.getName());
                note.setRoleName(row.getRoleName());
                note.setInitialStatement(row.getInitialStatement());

                noteMap.put(row.getSuspectId(), note);
            }

            // 현재 행의 질문·답변을 해당 용의자의 목록에 추가한다.
            StatementNoteItemRes statement = new StatementNoteItemRes();
            statement.setQuestionId(row.getQuestionId());
            statement.setQuestionText(row.getQuestionText());
            statement.setAnswerText(row.getAnswerText());

            note.getStatements().add(statement);
        }

        return List.copyOf(noteMap.values());
    }
}
