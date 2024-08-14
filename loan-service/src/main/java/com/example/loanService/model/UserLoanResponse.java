package com.example.loanService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoanResponse {
    private String loanNumber;
    private String loanStatus;
    private double loanAmount;
}