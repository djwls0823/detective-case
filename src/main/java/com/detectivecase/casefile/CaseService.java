package com.detectivecase.casefile;

import com.detectivecase.casefile.model.CaseGetListRes;
import com.detectivecase.player.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseService {

    private final CaseMapper caseMapper;
    private final PlayerMapper playerMapper;

    public List<CaseGetListRes> getCases(String playerId) {
        if (!playerMapper.existsById(playerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 플레이어입니다."
            );
        }

        return caseMapper.findAllByPlayerId(playerId);
    }
}
