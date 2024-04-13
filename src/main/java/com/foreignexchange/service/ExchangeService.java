package com.foreignexchange.service;

import com.foreignexchange.api.dto.CurrencyConvertRequest;
import com.foreignexchange.api.dto.CurrencyConvertResponse;
import com.foreignexchange.exception.ExchangeDomainException;
import com.foreignexchange.api.mapper.DtoMapper;
import com.foreignexchange.client.FastForexClient;
import com.foreignexchange.client.dto.ClientCurrencyConvertResponse;
import com.foreignexchange.config.FastForexProperties;
import com.foreignexchange.model.CurrencyConvert;
import com.foreignexchange.api.dto.ExchangeRate;
import com.foreignexchange.repository.CurrencyConvertRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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

    public List<CurrencyConvertResponse> findAllCurrencyConvert() {

        List<CurrencyConvert> all = currencyConvertRepository.findAll();
        List<CurrencyConvertResponse> response = new ArrayList<>();
        all.forEach(e -> {
            CurrencyConvertResponse build = mapToCurrencyResponse(e);
            response.add(build);
        });
        return response;
    }

    private CurrencyConvertResponse mapToCurrencyResponse(CurrencyConvert e) {
        CurrencyConvertResponse build = CurrencyConvertResponse.builder()
                .transactionId(e.getId())
                .sourceCurrency(e.getSourceCurrency())
                .targetCurrency(e.getTargetCurrency())
                .sourceAmount(e.getSourceAmount())
                .targetAmount(e.getTargetAmount())
                .rate(e.getRate())
                .build();
        return build;
    }

    public CurrencyConvertResponse findById(UUID id) {

        Optional<CurrencyConvert> byId = Optional.ofNullable(currencyConvertRepository.findById(id)
                .orElseThrow(() -> new ExchangeDomainException("Invalid transaction id {%s}".formatted(id))));
        return mapToCurrencyResponse(byId.get());
    }

    public List<CurrencyConvertResponse> sortByField(String field) {

        List<CurrencyConvertResponse> response = new ArrayList<>();
        List<CurrencyConvert> result = currencyConvertRepository.findAll(Sort.by(field));

        result.forEach(e -> response.add(mapToCurrencyResponse(e)));

        return response;
    }

    public Page<CurrencyConvertResponse> findAll(Pageable pageable) {

        Page<CurrencyConvert> all = currencyConvertRepository.findAll(pageable);

        return all.map(this::mapToCurrencyResponse);
    }
}
