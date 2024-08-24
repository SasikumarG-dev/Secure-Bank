package com.example.loanService.exceptions;

import com.example.loanService.dto.ApiResponse;
import com.example.loanService.dto.ErrorDetails;
import com.example.loanService.model.ResponseDto;
import com.example.loanService.model.ResponseHandler;
import com.example.loanService.util.ApplicationConstant;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.security.auth.login.AccountNotFoundException;
import java.security.SignatureException;
import java.util.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ResponseDto<Object>> handleInvalidData(InvalidDataException ex) {
        ErrorDetails error = new ErrorDetails(ex.getErrorCode(), ex.getErrorMessage());
        logger.error("Invalid Data Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.forError(Arrays.asList(error)));
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails("1001", usernameNotFoundException.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(ApplicationConstant.FAILURE, null, errors));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialException(BadCredentialsException badCredentialsException) {
        log.error("throwing badCredential exception");
        log.error(badCredentialsException.getMessage());
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails("1001", badCredentialsException.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(ApplicationConstant.FAILURE, null, errors));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentValidation(MethodArgumentNotValidException exception) {
        log.error("throwing methodArgumentValidation exception");
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails("1001", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(ApplicationConstant.FAILURE, null, errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("throwing httpMessageNotReadableException exception");
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails("1001", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(ApplicationConstant.FAILURE, null, errors));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> nullPointerException(NullPointerException exception) {
        log.error("throwing NullPointer exception");
        List<ErrorDetails> errors = new ArrayList<>();
        errors.add(new ErrorDetails("1001", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(ApplicationConstant.FAILURE, null, errors));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> token(AccessDeniedException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", exception.getMessage());
        logger.error("Access Not Denied Exception");
        return ResponseHandler.createErrorResponse("403", errorMap, HttpStatus.FORBIDDEN);

    }


    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> tokenSignature(SignatureException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", exception.getMessage());
        logger.error("Invalid token Signature");
        return ResponseHandler.createErrorResponse("400", errorMap, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> tokenExpired(ExpiredJwtException exception) {

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("message", exception.getMessage());
        logger.error("Jwt token expired");
        return ResponseHandler.createErrorResponse("400", errorMap, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> tokenMalfunction(MalformedJwtException ex) {


        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("message", "Invalid Token");
        logger.error("Token Mal Exception");
        return ResponseHandler.createErrorResponse("400", errorMap, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFound(AccountNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", ex.getMessage());
        logger.error("Account Not Exception");
        return ResponseHandler.createErrorResponse("400", errorMap, HttpStatus.BAD_REQUEST);
    }
}