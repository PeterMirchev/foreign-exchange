package com.foreignexchange.service;

import com.foreignexchange.client.FastForexClient;
import com.foreignexchange.config.FastForexProperties;
import com.foreignexchange.dto.ExchangeRate;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {

    private final FastForexClient fastForexClient;
    private final FastForexProperties fastForexProperties;

    public ExchangeService(FastForexClient fastForexClient, FastForexProperties fastForexProperties) {

        this.fastForexClient = fastForexClient;
        this.fastForexProperties = fastForexProperties;
    }

    public ExchangeRate getExchangeRate(String from, String to) {

        return fastForexClient.fetchOne(from, to, fastForexProperties.getApiKey());
    }
}
