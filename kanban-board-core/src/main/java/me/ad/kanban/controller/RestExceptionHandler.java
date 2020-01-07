package me.ad.kanban.controller;

import me.ad.kanban.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NoSuchElementException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(
            RuntimeException ex, WebRequest request) {
        /*ErrorDto err = new ErrorDto();
        err.setStatus(HttpStatus.NOT_FOUND.toString());
        err.setErrorCode(ex.getClass().getSimpleName());
        err.setErrorMessage(ex.getMessage());*/
        return handleExceptionInternal(ex, ex,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(
            RuntimeException ex, WebRequest request) {
        /*ErrorDto err = new ErrorDto();
        err.setStatus(HttpStatus.BAD_REQUEST.name());
        err.setErrorCode(ex.getClass().getSimpleName());
        err.setErrorMessage(ex.getMessage());*/
        return handleExceptionInternal(ex, ex,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
