package com.foreignexchange.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public class CurrencyConvertRequest {

    @NotNull
    private Currency from;
    @NotNull
    private Currency to;
    @NotNull
    @Positive
    private BigDecimal amount;
}
