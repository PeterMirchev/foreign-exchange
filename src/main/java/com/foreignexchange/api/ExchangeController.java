package com.foreignexchange.api;

import com.foreignexchange.api.dto.CurrencyConvertRequest;
import com.foreignexchange.api.dto.CurrencyConvertResponse;
import com.foreignexchange.api.dto.ExchangeRate;
import com.foreignexchange.config.PaginationProperties;
import com.foreignexchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Controller")
@OpenAPIDefinition(
        info = @Info(
                title = "Currency Exchange REST API Documentation",
                description = "Restful Realtime Calls for currency exchange Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Petar Mirchev",
                        email = "pet.mirchev89@gmail.com",
                        url = "https://github.com/PeterMirchev"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.currency-exchange.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Currency Exchange REST API Documentation",
                url = "https://www.fast-forex-exchange.com/swagger-ui.html"
        )
)
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final PaginationProperties paginationProperties;

    public ExchangeController(ExchangeService exchangeService,
                              PaginationProperties paginationProperties) {
        this.exchangeService = exchangeService;
        this.paginationProperties = paginationProperties;
    }

    @Operation(
            summary = "Get Exchange Rate REST API",
            description = "REST API to get exchange rate"
    )
    @GetMapping("/fetch-one")//localhost:8080/api/v1/foreign-exchange/fetch-one?from=USD&to=EUR
    public ResponseEntity<ExchangeRate> getExchangeRate(@RequestParam("from") Currency from,
                                                        @RequestParam("to") Currency to) {

        ExchangeRate exchangeRate = exchangeService.getExchangeRate(from, to);
        return ResponseEntity.ok(exchangeRate);
    }

    @Operation(
            summary = "Get all currency convert transactions REST API",
            description = "REST API to get all currency covert transactions"
    )
    @GetMapping("/transactions")//localhost:8080/api/v1/foreign-exchange/transactions?page=4
    public List<CurrencyConvertResponse> getAllCurrencyConvertTransactions(@RequestParam(name = "page", defaultValue = "0") Integer page) {

        Pageable pageable = PageRequest.of(page, paginationProperties.getSize());
        Page<CurrencyConvertResponse> pageCurrencyConvert = exchangeService.findAll(pageable);
        return pageCurrencyConvert.getContent();
    }

    @Operation(
            summary = "Get transaction by ID REST API",
            description = "REST API to get a transaction by ID"
    )
    @GetMapping("/{id}")//localhost:8080/api/v1/foreign-exchange/a072c1b8-e612-40bc-9a14-81d6b1dd31d0
    public ResponseEntity<CurrencyConvertResponse> getById(@Valid @PathVariable("id") UUID id) {

        CurrencyConvertResponse byId = exchangeService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @Operation(
            summary = "Get all transactions and sort by field REST API",
            description = "REST API to get all transactions and sort by field"
    )
    @GetMapping("/sorted/{field}")//localhost:8080/api/v1/foreign-exchange/sorted/createdOn
    public ResponseEntity<List<CurrencyConvertResponse>> sortByField(@Validated @PathVariable("field") String field) {

        List<CurrencyConvertResponse> response = exchangeService.sortByField(field);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Post transaction currency convert REST API",
            description = "REST API to post currency convert"
    )
    @PostMapping("/convert")//localhost:8080/api/v1/foreign-exchange/convert
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrencyConvertResponse> convertCurrency(@Validated @RequestBody CurrencyConvertRequest currencyConvertRequest) {

        CurrencyConvertResponse response = exchangeService.convertCurrency(currencyConvertRequest);
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
