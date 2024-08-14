package com.example.registrationService.dto.response;

import lombok.Data;

@Data
public class Error<T> {
    private T errorCode;
    private Object msg;
}