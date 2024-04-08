package com.foreignexchange.api;

import com.foreignexchange.dto.ExchangeRate;
import com.foreignexchange.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.foreignexchange.api.Paths.BASE_PATH_V1;

@RestController
@RequestMapping(BASE_PATH_V1 + "/foreign-exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/fetch-one")
    public ResponseEntity<ExchangeRate> getExchangeRate(@RequestParam("from") String from, @RequestParam("to") String to) {

        ExchangeRate exchangeRate = exchangeService.getExchangeRate(from, to);
        return ResponseEntity.ok(exchangeRate);
    }
}
