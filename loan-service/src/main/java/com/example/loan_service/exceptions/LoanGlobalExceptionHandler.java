package com.example.loan_service.exceptions;

import com.example.loan_service.model.ResponseDto;
import com.example.loan_service.model.ResponseHandler;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.security.auth.login.AccountNotFoundException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class LoanGlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(LoanGlobalExceptionHandler.class);

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ResponseDto<Object>> handleInvalidData(InvalidDataException ex){
        ErrorDetails error=new ErrorDetails(ex.getErrorCode(), ex.getErrorMessage());
        logger.error("Invalid Data Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.forError(Arrays.asList(error)));
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<ResponseDto<Object>> feignExceptionHandle(FeignException.BadRequest ex) {
        String message = null;
        String code = "400";
        String exceptionMessage = ex.getMessage();
        Pattern pattern = Pattern.compile("\"message\":\"(.*?)\",\"code\":\"(.*?)\"}");
        Matcher matcher = pattern.matcher(exceptionMessage);
        if (matcher.find()) {
            message = matcher.group(1);
            code = matcher.group(2);
        }
        logger.error("Feign Client Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.forError(Arrays.asList(new ErrorDetails(code,message))));
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<?> serviceUnavailable(FeignException.ServiceUnavailable ex) {
        logger.error("Service unavailable retrying ");
        return ResponseHandler.createErrorResponse("503", "Service Unavailable try after some time", HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    public ResponseEntity<ResponseDto<Object>> handleInvalidDataType(HttpMessageNotReadableException ex) {

        ErrorDetails error = new ErrorDetails("4001", "Invalid Type");
        logger.error("Invalid Data type");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.forError(error));

    }
    @ExceptionHandler(value = ClientException.class)
    public ResponseEntity<ResponseDto<Object>> handleClientException(ClientException ex) {
        ErrorDetails error = new ErrorDetails(ex.getErrorCode(), ex.getErrorMessage());
        logger.error("Feign Client Exception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDto.forError(Arrays.asList(error)));
    }
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<ResponseDto<Object>> handleMethodArgumentException(MethodArgumentNotValidException exception,
                                                                             WebRequest request) {

        Map<String, String> errorDataMap = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorDataMap.put(error.getField(), error.getDefaultMessage());
        });

        ErrorDetails error = new ErrorDetails(errorDataMap.toString(),request.getDescription(false));
        logger.error("Method Argument Not valid Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.forError(error));

    }
    @ExceptionHandler(AccessDeniedException.class)

    public ResponseEntity<?> token(AccessDeniedException exception) {

        Map<String,String> errorMap = new HashMap<>();

        errorMap.put("message", exception.getMessage());
        logger.error("Access Not Denied Exception");
        return ResponseHandler.createErrorResponse("403", errorMap, HttpStatus.FORBIDDEN);

    }



    @ExceptionHandler(SignatureException.class)

    public ResponseEntity<?> tokenSignature(SignatureException exception) {

        Map<String,String> errorMap = new HashMap<>();

        errorMap.put("message", exception.getMessage());
        logger.error("Invalid token Signature");
        return ResponseHandler.createErrorResponse("400", errorMap, HttpStatus.BAD_REQUEST);

    }



//
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<?> tokenExpired(ExpiredJwtException exception) {
//
//        Map<String,String> errorMap = new HashMap<>();
//
//        errorMap.put("message", exception.getMessage());
//        logger.error("Jwt token expired");
//        return ResponseHandler.createErrorResponse("400", errorMap, HttpStatus.BAD_REQUEST);
//
//    }
//    @ExceptionHandler(MalformedJwtException.class)
//
//    public ResponseEntity<?> tokenMalfunction(MalformedJwtException ex) {
//
//
//        Map<String,String> errorMap = new HashMap<>();
//
//        errorMap.put("message", "Invalid Token");
//        logger.error("Token Mal Exception");
//        return ResponseHandler.createErrorResponse("400", errorMap, HttpStatus.BAD_REQUEST);
//
//    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFound(AccountNotFoundException ex) {
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("message", ex.getMessage());
        logger.error("Account Not Exception");
        return ResponseHandler.createErrorResponse("400", errorMap, HttpStatus.BAD_REQUEST);
    }
}