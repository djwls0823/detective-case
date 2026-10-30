package com.detectivecase.casefile.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaseGetListRes {
    private Long caseId;
    private String title;
    private Boolean isUnlocked;
}
