package com.foreignexchange.api;

import com.foreignexchange.api.dto.CurrencyConvertRequest;
import com.foreignexchange.api.dto.CurrencyConvertResponse;
import com.foreignexchange.api.dto.ExchangeRate;
import com.foreignexchange.config.PaginationProperties;
import com.foreignexchange.service.ExchangeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static com.foreignexchange.api.Paths.BASE_PATH_V1;

@RestController
@RequestMapping(BASE_PATH_V1 + "/foreign-exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final PaginationProperties paginationProperties;

    public ExchangeController(ExchangeService exchangeService,
                              PaginationProperties paginationProperties) {
        this.exchangeService = exchangeService;
        this.paginationProperties = paginationProperties;
    }

    @GetMapping("/fetch-one")//localhost:8080/api/v1/foreign-exchange/fetch-one?from=USD&to=EUR
    public ResponseEntity<ExchangeRate> getExchangeRate(@RequestParam("from") Currency from,
                                                        @RequestParam("to") Currency to) {

        ExchangeRate exchangeRate = exchangeService.getExchangeRate(from, to);
        return ResponseEntity.ok(exchangeRate);
    }

    @GetMapping("/transactions")//localhost:8080/api/v1/foreign-exchange/transactions?page=4
    public List<CurrencyConvertResponse> getAllCurrencyConvertTransactions(@RequestParam(name = "page", defaultValue = "0") Integer page) {

        Pageable pageable = PageRequest.of(page, paginationProperties.getSize());
        Page<CurrencyConvertResponse> pageCurrencyConvert = exchangeService.findAll(pageable);
        return pageCurrencyConvert.getContent();
    }

    @GetMapping("/{id}")//localhost:8080/api/v1/foreign-exchange/a072c1b8-e612-40bc-9a14-81d6b1dd31d0
    public ResponseEntity<CurrencyConvertResponse> getById(@Valid @PathVariable("id") UUID id) {

        CurrencyConvertResponse byId = exchangeService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @GetMapping("/sorted/{field}")//localhost:8080/api/v1/foreign-exchange/sorted/createdOn
    public ResponseEntity<List<CurrencyConvertResponse>> sortByField(@Validated @PathVariable("field") String field) {

        List<CurrencyConvertResponse> response = exchangeService.sortByField(field);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert")//localhost:8080/api/v1/foreign-exchange/convert
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrencyConvertResponse> convertCurrency(@Validated @RequestBody CurrencyConvertRequest currencyConvertRequest) {

        CurrencyConvertResponse response = exchangeService.convertCurrency(currencyConvertRequest);
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
