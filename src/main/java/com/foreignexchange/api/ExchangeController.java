package com.foreignexchange.api;

import com.foreignexchange.api.dto.CurrencyConvertRequest;
import com.foreignexchange.api.dto.CurrencyConvertResponse;
import com.foreignexchange.api.dto.ExchangeRate;
import com.foreignexchange.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

import static com.foreignexchange.api.Paths.BASE_PATH_V1;

@RestController
@RequestMapping(BASE_PATH_V1 + "/foreign-exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/fetch-one")//localhost:8080/api/v1/foreign-exchange/fetch-one?from=USD&to=EUR
    public ResponseEntity<ExchangeRate> getExchangeRate(@RequestParam("from") Currency from,
                                                        @RequestParam("to") Currency to) {

        ExchangeRate exchangeRate = exchangeService.getExchangeRate(from, to);
        return ResponseEntity.ok(exchangeRate);
    }

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConvertResponse> convertCurrency(@Validated @RequestBody CurrencyConvertRequest currencyConvertRequest) {

        CurrencyConvertResponse response = exchangeService.convertCurrency(currencyConvertRequest);
        return  ResponseEntity.ok(response);
    }
}
