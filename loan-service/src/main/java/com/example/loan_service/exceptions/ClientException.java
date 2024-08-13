package com.example.loan_service.exceptions;

import lombok.Data;

@Data
public class ClientException extends RuntimeException {
    private String errorCode;

    private String errorMessage;
    public ClientException(String message) {

        this.errorCode = "4001";
        this.errorMessage = message;
    }
}

