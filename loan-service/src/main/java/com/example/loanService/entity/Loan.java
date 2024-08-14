package com.example.loanService.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Loan {
    @Id
    private String loanNumber;
    private Integer userId;
    private double assetValue;
    private double loanAmount;
    private double payableAmount;
    private double emiAmount;
    private double interestRate;
    private String reason;
    private int installments;
    private int pendingInstallments;
    private String loanStatus;
    private boolean firstLevelApproval;
    private boolean secondLevelApproval;
    private LocalDate startDate;
    private LocalDate endDate;
}