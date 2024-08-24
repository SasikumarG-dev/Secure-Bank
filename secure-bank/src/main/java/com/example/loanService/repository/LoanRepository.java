package com.example.loanService.repository;

import com.example.loanService.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,String> {
    Optional<Loan> findByLoanNumber(String loanNumber);
}