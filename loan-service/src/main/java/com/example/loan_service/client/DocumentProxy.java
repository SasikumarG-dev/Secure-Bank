package com.example.loan_service.client;


import com.example.loan_service.model.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="DOCUMENT-SERVICE")
public interface DocumentProxy {

    @GetMapping("/document/check/{loanNumber}")
    ResponseEntity<ResponseDto<Boolean>> isDocumentUploaded(
            @RequestHeader("Authorization") String token,
            @PathVariable("loanNumber") String loanNumber
    );
}



