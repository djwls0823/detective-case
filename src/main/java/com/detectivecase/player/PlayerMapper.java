package com.detectivecase.player;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlayerMapper {
    int insert(@Param("playerId") String playerId);

    boolean existsById(@Param("playerId") String playerId);
}
