package com.detectivecase.hint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class HintHistoryGetRes {
    private List<HintGetRes> hints;
    private int hintCount;
    private int remainingHintCount;
    private int score;
}
