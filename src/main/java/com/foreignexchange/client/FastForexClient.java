package com.foreignexchange.client;

import com.foreignexchange.client.dto.ClientCurrencyConvertResponse;
import com.foreignexchange.api.dto.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Currency;

@FeignClient(name = "fastForexClient", url = "https://api.fastforex.io")
public interface FastForexClient {

    @GetMapping("/fetch-one")
    ExchangeRate fetchOne(@RequestParam("from") Currency from,
                          @RequestParam("to") Currency to,
                          @RequestParam("api_key")String apiKey);

    @GetMapping("/convert")
    ClientCurrencyConvertResponse convert(@RequestParam("from") Currency from,
                                          @RequestParam("to") Currency to,
                                          @RequestParam("amount") BigDecimal amount,
                                          @RequestParam("api_key")String apiKey);

}
