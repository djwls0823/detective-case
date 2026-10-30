package com.detectivecase.casefile;

import com.detectivecase.casefile.model.CaseGetListRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CaseMapper {
    List<CaseGetListRes> findAllByPlayerId(@Param("playerId") String playerId);
}