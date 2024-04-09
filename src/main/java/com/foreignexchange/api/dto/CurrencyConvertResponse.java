package com.foreignexchange.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Data
@Builder
public class CurrencyConvertResponse {

    private UUID transactionId;
    private Currency sourceCurrency;
    private BigDecimal sourceAmount;
    private Currency targetCurrency;
    private BigDecimal targetAmount;
    private BigDecimal rate;
}
