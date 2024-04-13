package com.foreignexchange.api;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.foreignexchange.exception.ExchangeDomainException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionAdvise {

    private static final String BAD_REQUEST = "123";
    private static final String INVALID_TRANSACTION_ID = "405";
    private static final String INTERNAL_SERVER_ERROR = "1b38fe4a-9c31-11ee-8c90-0242ac120002";


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> internalServerError(Throwable throwable) {

        return getErrorResponse(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage());
    }


    @ExceptionHandler({
            HttpMediaTypeException.class,
            ServletRequestBindingException.class,
            IllegalArgumentException.class,
            JsonMappingException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class
    })
    public ResponseEntity<ErrorResponse> badRequest(Throwable throwable) {

        return getErrorResponse(BAD_REQUEST, HttpStatus.BAD_REQUEST, throwable.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> badRequest(MethodArgumentNotValidException exception) {

        String[] message = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toArray(String[]::new);
        return getErrorResponse(BAD_REQUEST, HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler({EntityNotFoundException.class,
            ExchangeDomainException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> invalidTransactionId(Throwable throwable) {

        return getErrorResponse(INVALID_TRANSACTION_ID, HttpStatus.BAD_REQUEST, throwable.getMessage());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> invalidTransactionId(HttpMessageNotReadableException error) {

        String message = error.getCause().getMessage().split(":")[0];
        return getErrorResponse(INVALID_TRANSACTION_ID, HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(String id, HttpStatus httpStatus, String... message) {

        final ErrorResponse error =ErrorResponse.builder()
                .errorCode(id)
                .time(LocalDateTime.now())
                .messages(message)
                .build();

        return new ResponseEntity<>(error, httpStatus);

    }

}
