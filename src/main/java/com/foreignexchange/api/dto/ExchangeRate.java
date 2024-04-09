package com.foreignexchange.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Builder
@Data
public class ExchangeRate {

    private Currency base;
    private Map<Currency, BigDecimal> result;
    private String updated;

}
