package com.foreignexchange.service;

import com.foreignexchange.api.dto.CurrencyConvertRequest;
import com.foreignexchange.api.dto.CurrencyConvertResponse;
import com.foreignexchange.api.mapper.DtoMapper;
import com.foreignexchange.client.FastForexClient;
import com.foreignexchange.client.dto.ClientCurrencyConvertResponse;
import com.foreignexchange.config.FastForexProperties;
import com.foreignexchange.model.CurrencyConvert;
import com.foreignexchange.api.dto.ExchangeRate;
import com.foreignexchange.repository.CurrencyConvertRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
public class ExchangeService {

    private final FastForexClient fastForexClient;
    private final FastForexProperties fastForexProperties;

    private final CurrencyConvertRepository currencyConvertRepository;

    public ExchangeService(FastForexClient fastForexClient,
                           FastForexProperties fastForexProperties,
                           CurrencyConvertRepository currencyConvertRepository) {

        this.fastForexClient = fastForexClient;
        this.fastForexProperties = fastForexProperties;
        this.currencyConvertRepository = currencyConvertRepository;
    }

    public ExchangeRate getExchangeRate(Currency from, Currency to) {

        return fastForexClient.fetchOne(from, to, fastForexProperties.getApiKey());
    }

    public CurrencyConvertResponse convertCurrency(CurrencyConvertRequest currencyConvertRequest) {

        Currency from = currencyConvertRequest.getFrom();
        Currency to = currencyConvertRequest.getTo();
        BigDecimal amount = currencyConvertRequest.getAmount();

        ClientCurrencyConvertResponse clientResponse = fastForexClient.convert(from, to, amount, fastForexProperties.getApiKey());
        CurrencyConvert currencyConvert = CurrencyConvert.builder()
                .sourceCurrency(from)
                .targetCurrency(to)
                .sourceAmount(amount)
                .targetAmount(clientResponse.getResult().get(to.getCurrencyCode()))
                .rate(clientResponse.getResult().get("rate"))
                .build();

        CurrencyConvert persistedCurrencyConvert = currencyConvertRepository.save(currencyConvert);

        return DtoMapper.mapToCurrencyConvertResponse(persistedCurrencyConvert);
    }
}
