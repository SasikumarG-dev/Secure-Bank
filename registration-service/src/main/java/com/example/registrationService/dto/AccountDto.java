package com.example.registrationService.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    @Id
    private String accountNumber;
    private Integer userId;
    private Double accountBalance;
    private String accountStatus;
    private String kyc;
    private Integer transactionCount;
}

