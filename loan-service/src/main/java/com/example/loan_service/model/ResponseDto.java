package com.example.loan_service.model;

import lombok.Data;

@Data
public class ResponseDto<T> {

    public ResponseDto(String status, T data, T error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    private String status;
    private T data;
    private T error;


    public static <T> ResponseDto<T> forSuccess(T data) {
        return new ResponseDto<>("SUCCESS", data, null);
    }

    public static<T> ResponseDto<T> forError(T errors) {
        return new ResponseDto<>("ERROR", null, errors);
    }

}
