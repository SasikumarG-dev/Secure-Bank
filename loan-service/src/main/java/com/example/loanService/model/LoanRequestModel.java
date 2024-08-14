package com.example.loanService.model;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanRequestModel {
    @Positive(message = "Enter valid amount")
    private double assetValue;
    @Positive(message = "Enter valid amount")
    private double loanAmount;
    @Positive(message = "Enter valid number")
    private int installments;
}
