package com.example.loan_service.service;

import com.example.loan_service.entity.Loan;
import com.example.loan_service.exceptions.ClientException;
import com.example.loan_service.model.LoanRequestModel;
import com.example.loan_service.model.LoanResponseModel;
import com.example.loan_service.model.LoanStatusModel;
import com.example.loan_service.model.UserLoanResponse;

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
