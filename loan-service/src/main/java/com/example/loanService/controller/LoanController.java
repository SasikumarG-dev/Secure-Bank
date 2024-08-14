package com.example.loanService.controller;

import com.example.loanService.entity.Loan;
import com.example.loanService.model.LoanRequestModel;
import com.example.loanService.model.LoanResponseModel;
import com.example.loanService.model.ResponseDto;
import com.example.loanService.service.LoanService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
@Data
public class LoanController {
    private static final Logger log = LoggerFactory.getLogger(LoanController.class);
    @NonNull
    private LoanService loanService;
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    private Authentication authentication;
    @PostMapping("/apply")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseDto<LoanResponseModel>> applyLoan(@RequestBody @Valid LoanRequestModel loanDto){
        authentication= SecurityContextHolder.getContext().getAuthentication();
        String userID=authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.forSuccess(loanService.applyLoan(loanDto,userID)));
    }


    @GetMapping("/{loanId}")
    @PreAuthorize("hasAuthority('USER')" )
    public ResponseEntity<ResponseDto<Optional<Loan>>> getLoanDetailsByLoanId(@RequestHeader("Authorization") String token, @PathVariable String loanId){
        authentication= SecurityContextHolder.getContext().getAuthentication();
        String userID=authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.forSuccess(loanService.getLoanByLoadId(loanId, userID)));

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')" )
    public ResponseEntity<ResponseDto<Object>> getAllLoans(){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.forSuccess(loanService.getAllLoans()));

    }

//    @PutMapping("/status")
//    @PreAuthorize("hasAuthority('ADMIN')" )
//    @CircuitBreaker(name = "fallback", fallbackMethod = "defaultMethod")
//    public ResponseEntity<ResponseDto<Object>> updateStatus(@RequestHeader("Authorization") String token,@RequestBody LoanStatusModel loanStatusDto){
//        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.forSuccess(loanService.updateStatus(loanStatusDto,token)));
//
//    }
    @PostMapping("/save/transaction")
    public ResponseEntity<Loan> updateTransaction(@RequestBody Loan loan){
        return ResponseEntity.status(HttpStatus.OK).body(loanService.saveLoanDetails(loan));
    }

    @GetMapping("/status/{loanNumber}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ResponseDto<Boolean>> getLoanStatus(@PathVariable("loanNumber") String loanNumber){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.forSuccess(loanService.getLoanStatus(loanNumber)));

    }
//    public ResponseEntity<ResponseDto<String>> defaultMethod(CallNotPermittedException exception){
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ResponseDto.forSuccess("Service unavailable try after some time"));
//    }
}