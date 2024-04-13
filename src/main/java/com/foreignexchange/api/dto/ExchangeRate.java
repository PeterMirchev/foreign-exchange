package com.foreignexchange.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    private Currency base;
    private Map<Currency, BigDecimal> result;
    private String updated;

}
