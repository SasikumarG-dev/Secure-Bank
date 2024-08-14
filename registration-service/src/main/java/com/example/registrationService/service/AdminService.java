package com.example.registrationService.service;

import com.example.registrationService.dto.AccountDto;
import com.example.registrationService.dto.ProspectApplicationDto;
import com.example.registrationService.exception.ApplicationNumberNotFoundException;

import java.util.List;

public interface AdminService {
    List<ProspectApplicationDto> getAllPendingApplications();
    String approvePendingCreditCardApplication(String applicationNumber) throws ApplicationNumberNotFoundException;
    String rejectPendingApplication(String applicationNumber) throws ApplicationNumberNotFoundException;
    AccountDto approvePendingOnlineBankingApplication(String applicationNumber) throws ApplicationNumberNotFoundException;

}
