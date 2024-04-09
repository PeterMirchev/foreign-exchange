package com.foreignexchange.api.mapper;

import com.foreignexchange.api.dto.CurrencyConvertResponse;
import com.foreignexchange.model.CurrencyConvert;

public class DtoMapper {

    public static CurrencyConvertResponse mapToCurrencyConvertResponse(CurrencyConvert model) {

        return CurrencyConvertResponse.builder()
                .transactionId(model.getId())
                .sourceCurrency(model.getSourceCurrency())
                .sourceAmount(model.getSourceAmount())
                .targetCurrency(model.getTargetCurrency())
                .targetAmount(model.getTargetAmount())
                .rate(model.getRate())
                .build();
    }
}
