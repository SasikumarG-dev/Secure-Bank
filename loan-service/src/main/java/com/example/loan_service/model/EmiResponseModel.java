package com.example.loan_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmiResponseModel {
    private double emiAmount;
    private double interestRate;
    private double totalPayableAmount;
}