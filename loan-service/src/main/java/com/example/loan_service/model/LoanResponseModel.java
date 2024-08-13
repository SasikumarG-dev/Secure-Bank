package com.example.loan_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanResponseModel {
    private String loanNumber;
    private String url;
}
