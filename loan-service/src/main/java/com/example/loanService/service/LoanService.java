package com.example.loanService.service;

import com.example.loanService.entity.Loan;
import com.example.loanService.exceptions.ClientException;
import com.example.loanService.model.LoanRequestModel;
import com.example.loanService.model.LoanResponseModel;
import com.example.loanService.model.LoanStatusModel;
import com.example.loanService.model.UserLoanResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LoanService {
    public LoanResponseModel applyLoan(LoanRequestModel loanDto, String userID);

    public Optional<Loan> getLoanByLoadId(String loadId, String userId);

    public Map<Integer, List<UserLoanResponse>> getAllLoans();

    public String updateStatus(LoanStatusModel loanStatus, String token) throws ClientException;

    public Loan saveLoanDetails(Loan loan);

    public Boolean getLoanStatus(String loanNumber);

}
