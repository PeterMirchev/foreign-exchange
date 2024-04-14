package com.foreignexchange.api;

import com.foreignexchange.api.dto.ExchangeRate;
import com.foreignexchange.config.PaginationProperties;
import com.foreignexchange.service.ExchangeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeControllerTest {

    @Mock
    private ExchangeService exchangeService;

    @InjectMocks
    private ExchangeController exchangeController;

    @Nested
    @DisplayName("Tests for method getExchangeRate()")
    class GetExchangeRate {

        @Test
        void givenValidInput_thenReturnExchangeRate() {

            // given
            Currency usd = Currency.getInstance("USD");
            Currency eur = Currency.getInstance("EUR");
            when(exchangeService.getExchangeRate(usd, eur)).thenReturn(mock(ExchangeRate.class));

            // when
            ResponseEntity<ExchangeRate> exchangeRate = exchangeController.getExchangeRate(usd, eur);

            // then
            assertThat(exchangeRate).isNotNull();
        }

    }



}
