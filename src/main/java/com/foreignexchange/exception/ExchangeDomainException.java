package com.foreignexchange.exception;

public class ExchangeDomainException extends RuntimeException{

    public ExchangeDomainException() {

    }

    public ExchangeDomainException(String message) {

        super(message);
    }

    public ExchangeDomainException(String message, Throwable throwable) {

        super(message, throwable);
    }
}
