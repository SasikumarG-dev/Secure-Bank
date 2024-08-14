package com.example.registrationService.service.impl;


import com.example.registrationService.dto.AccountDto;
import com.example.registrationService.dto.GetUserDetailDto;
import com.example.registrationService.dto.ProspectApplicationDto;
import com.example.registrationService.entity.ProspectApplication;
import com.example.registrationService.exception.ApplicationNumberNotFoundException;
import com.example.registrationService.feign.CreditCardFeign;
import com.example.registrationService.feign.FeignService;
import com.example.registrationService.repository.ProspectRepository;
import com.example.registrationService.service.AdminService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    @NonNull
    private final CreditCardFeign creditCardFeign;
    @NonNull
    private final ProspectRepository prospectRepository;
    @NonNull
    private final FeignService feignService;
    @Value("${approved}")
    private String APPROVED;
    @Value("${rejected}")
    private String REJECTED;
    @Value("${pending}")
    private String PENDING;

    public List<ProspectApplicationDto> getAllPendingApplications() {
        List<ProspectApplication> prospectApplications = prospectRepository.findAllByStatus(PENDING);
        List<ProspectApplicationDto> prospectApplicationDtoList = prospectApplications
                .stream().map(prospectApplication ->
                        new ProspectApplicationDto(prospectApplication.getApplicationNumber()
                                ,prospectApplication.getType())).toList();
        log.info("Fetched SuccessFully");
        return prospectApplicationDtoList;
    }
    public String approvePendingCreditCardApplication(String applicationNumber) throws ApplicationNumberNotFoundException {
        String type = null;
        ProspectApplication prospectApplication = prospectRepository.findByApplicationNumberAndStatusAndType(applicationNumber,
                PENDING,"Credit-Card");
        if(prospectApplication!=null){
            prospectApplication.setStatus(APPROVED);
            prospectApplication.setModifiedName("admin");
            prospectApplication.setModifiedDate(LocalDate.now());
            type = generateCreditCardNumber(applicationNumber);
            prospectRepository.save(prospectApplication);
            return type;
        }
        else {
            throw new ApplicationNumberNotFoundException("Application Number Not Found");
        }
    }
    public String rejectPendingApplication(String applicationNumber) throws ApplicationNumberNotFoundException {
        ProspectApplication prospectApplication = prospectRepository.findByApplicationNumberAndStatus(applicationNumber,
                PENDING);
        if(prospectApplication==null){
            log.error("Application Number Not Found");
            throw new ApplicationNumberNotFoundException("Application Number Not Found");
        }
        prospectApplication.setStatus(REJECTED);
        prospectApplication.setModifiedName("Admin");
        prospectApplication.setModifiedDate(LocalDate.now());
        prospectRepository.save(prospectApplication);
        log.info("Rejected SuccessFully");
        return "Rejected Successfully "+applicationNumber;
    }

    String generateCreditCardNumber(String applicationNumber){
        String cardNumber = creditCardFeign.approvePendingApplication(applicationNumber);
        return cardNumber;
    }
    AccountDto generateAccountNumber(GetUserDetailDto userId){
        AccountDto accountDto = feignService.approvePendingAccount(userId);
        return accountDto;
    }
    public AccountDto approvePendingOnlineBankingApplication(String applicationNumber) throws ApplicationNumberNotFoundException {
        String type= null;
        ProspectApplication prospectApplication = prospectRepository.findByApplicationNumberAndStatusAndType(applicationNumber,
                PENDING,"Online-Banking");
        if (prospectApplication!=null)
        {
            String userId = prospectApplication.getUser();
            prospectApplication.setStatus(APPROVED);
            prospectApplication.setModifiedName("admin");
            prospectApplication.setModifiedDate(LocalDate.now());
            GetUserDetailDto getUserDetailDto = new GetUserDetailDto().builder().userId(Integer.parseInt(userId)).build();
            AccountDto accountDto = generateAccountNumber(getUserDetailDto);
            prospectRepository.save(prospectApplication);
            return accountDto;
        }else {
            throw new ApplicationNumberNotFoundException("Application Number Not Found");
        }
    }
}


