package com.detectivecase.solution.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolutionKeywordRow {

    /*
     * 같은 groupId의 키워드 중 하나만 포함되어도
     * 해당 키워드 그룹은 충족한 것으로 판단한다.
     */
    private Long keywordGroupId;

    // 예: 정전, 차단기, 두꺼비집
    private String keywordText;
}