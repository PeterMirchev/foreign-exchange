package com.foreignexchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.Map;

@Builder
@Data
public class ExchangeRate {
    private Currency base;
    private Map<Currency, BigDecimal> result;
    private String updated;

}
