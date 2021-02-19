package com.hotelapp;

import com.hotelapp.daos.ErrorResponse;
import com.hotelapp.exceptions.DataDuplicateExceptoion;
import com.hotelapp.exceptions.DataNotFoundException;
import com.hotelapp.exceptions.OverrideNotAllowedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({DataDuplicateExceptoion.class,  OverrideNotAllowedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse dataExceptionsHandler(RuntimeException ex){
        return new ErrorResponse(ex.getMessage(),ex.getClass().getName());
    }

    @ExceptionHandler({ DataNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse dataNotFound(RuntimeException ex){
        return new ErrorResponse(ex.getMessage(),ex.getClass().getName());

    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorList.toString(),ex.getClass().getName()));
    }


}
