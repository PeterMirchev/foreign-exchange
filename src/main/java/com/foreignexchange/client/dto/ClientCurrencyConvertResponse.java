package com.foreignexchange.client.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Data
public class ClientCurrencyConvertResponse {

    private Currency base;
    private BigDecimal amount;
    private Map<String, BigDecimal> result;
}
