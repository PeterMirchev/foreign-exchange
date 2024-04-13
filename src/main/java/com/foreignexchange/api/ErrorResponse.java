package com.foreignexchange.api;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorResponse {

    private String errorCode;
    private LocalDateTime time;
    private String[] messages;
}
