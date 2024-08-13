package com.example.loan_service.client;
import com.example.loan_service.model.EmiRequestModel;
import com.example.loan_service.model.EmiResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="EMI-SERVICE")
public interface EmiProxy {
    @PostMapping("/emi/amount")
    ResponseEntity<EmiResponseModel> getEMIAmount(
            @RequestHeader("Authorization") String token,
            @RequestBody EmiRequestModel emiRequest
    );
}