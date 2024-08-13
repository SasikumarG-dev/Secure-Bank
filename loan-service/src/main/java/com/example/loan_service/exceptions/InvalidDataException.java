package com.example.loan_service.exceptions;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidDataException extends RuntimeException{

    private String errorCode;

    private String errorMessage;
    public InvalidDataException(String message) {

        this.errorCode = "4001";
        this.errorMessage = message;
    }

}