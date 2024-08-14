package com.example.registrationService.controller;

import com.example.registrationService.dto.AccountDto;
import com.example.registrationService.dto.ProspectApplicationDto;
import com.example.registrationService.dto.response.ApiResponse;
import com.example.registrationService.exception.ApplicationNumberNotFoundException;
import com.example.registrationService.service.AdminService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AdminController {
    @NonNull
    private AdminService adminService;
    @GetMapping("/application/status/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<List<ProspectApplicationDto>>> getAllPendingApplications() {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(adminService.getAllPendingApplications()));
    }
    @PutMapping("/{applicationNumber}/approve/credit-card")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<String>> approveCreditCardApplication(@PathVariable String applicationNumber) throws
            ApplicationNumberNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(adminService.approvePendingCreditCardApplication
                (applicationNumber)));
    }
    @PutMapping("/{applicationNumber}/approve/online-banking")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<AccountDto>> approveOnlineBankingApplication(@PathVariable String applicationNumber) throws
            ApplicationNumberNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(adminService.approvePendingOnlineBankingApplication
                (applicationNumber)));
    }
    @PutMapping("/{applicationNumber}/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<String>> rejectApplication(@PathVariable String applicationNumber) throws
            ApplicationNumberNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(adminService.rejectPendingApplication
                (applicationNumber)));
    }
}


