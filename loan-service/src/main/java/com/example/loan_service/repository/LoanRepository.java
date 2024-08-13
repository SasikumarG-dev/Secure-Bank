package com.example.loan_service.repository;

import com.example.loan_service.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,String> {
    Optional<Loan> findByLoanNumber(String loanNumber);
}