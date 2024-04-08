package com.foreignexchange.client;

import com.foreignexchange.dto.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fastForexClient", url = "https://api.fastforex.io")
public interface FastForexClient {

    @GetMapping("/fetch-one")
    ExchangeRate fetchOne(@RequestParam("from") String from,
                          @RequestParam("to") String to,
                          @RequestParam("api_key")String apiKey);

}
