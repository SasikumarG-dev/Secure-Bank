package com.example.loan_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanStatusModel {
    private String loanNumber;
    private String loanStatus;
    private String reason;
}
