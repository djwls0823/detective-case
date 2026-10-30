package com.detectivecase.player;

import com.detectivecase.player.model.PlayerCreateRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerMapper playerMapper;

    public PlayerCreateRes createPlayer() {
        String playerId = UUID.randomUUID().toString();

        playerMapper.insert(playerId);

        return new PlayerCreateRes(playerId);
    }
}