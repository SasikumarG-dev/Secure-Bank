package com.example.loanService.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanResponseModel {
    private String loanNumber;
    private String url;
}
