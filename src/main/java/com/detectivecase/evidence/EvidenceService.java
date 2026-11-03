package com.detectivecase.evidence;

import com.detectivecase.evidence.model.EvidenceGetListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceMapper evidenceMapper;

    /*
     * 증거 노트 화면에 표시할 증거 목록을 조회한다.
     *
     * 존재하지 않는 세션일 때 단순 빈 배열을 반환하면
     * 정상적인 사건에 증거가 없는 상태와 구분하기 어렵다.
     * 따라서 404 오류로 처리한다.
     */
    public List<EvidenceGetListRes> getEvidences(String sessionId) {
        if (!evidenceMapper.existsSessionById(sessionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 게임 세션입니다."
            );
        }

        return evidenceMapper.findAllBySessionId(sessionId);
    }
}
