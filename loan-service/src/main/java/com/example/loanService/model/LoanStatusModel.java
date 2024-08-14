package com.example.loanService.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanStatusModel {
    private String loanNumber;
    private String loanStatus;
    private String reason;
}
