package com.example.loanService.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Map<String, Object>> createErrorResponse(String code, Object message, HttpStatus status) {

        Map<String, Object> errorMap = new LinkedHashMap<>();

        List<Map<String, Object>> errorList = new ArrayList<>();

        errorList.add(errorMap);

        errorMap.put("code", code);

        errorMap.put("message", message);

        Map<String, Object> responseMap = new LinkedHashMap<>();

        responseMap.put("status", "Error");

        responseMap.put("data", null);

        responseMap.put("errors", errorList);

        return new ResponseEntity<>(responseMap, status);

    }
}
