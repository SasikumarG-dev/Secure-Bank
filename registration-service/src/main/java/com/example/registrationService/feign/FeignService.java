package com.example.registrationService.feign;
import com.example.registrationService.dto.AccountDto;
import com.example.registrationService.dto.GetUserDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface FeignService {

    @PostMapping("/account/create")
    AccountDto approvePendingAccount(@RequestBody GetUserDetailDto getUserDetailDto);

}