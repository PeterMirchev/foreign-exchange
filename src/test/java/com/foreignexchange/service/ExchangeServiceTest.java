package com.foreignexchange.service;

import com.foreignexchange.api.dto.CurrencyConvertRequest;
import com.foreignexchange.api.dto.CurrencyConvertResponse;
import com.foreignexchange.exception.ExchangeDomainException;
import com.foreignexchange.api.dto.ExchangeRate;
import com.foreignexchange.client.FastForexClient;
import com.foreignexchange.client.dto.ClientCurrencyConvertResponse;
import com.foreignexchange.config.FastForexProperties;
import com.foreignexchange.model.CurrencyConvert;
import com.foreignexchange.repository.CurrencyConvertRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    private FastForexClient fastForexClient;

    @Mock
    private FastForexProperties fastForexProperties;

    @Mock
    private CurrencyConvertRepository currencyConvertRepository;

    @InjectMocks
    private ExchangeService exchangeService;

    @Nested
    @DisplayName("Tests for method getExchangeRate()")
    class GetExchangeRate {

        @Test
        void givenValidInputCurrencies_thenReturnExchangeRate() {
            // given
            Currency from = Currency.getInstance("USD");
            Currency to = Currency.getInstance("EUR");
            String apiKey = "TestApiKey";

            when(fastForexProperties.getApiKey()).thenReturn(apiKey);
            when(fastForexClient.fetchOne(from, to, apiKey)).thenReturn(new ExchangeRate());

            // when
            ExchangeRate rate = exchangeService.getExchangeRate(from, to);

            // then
            assertThat(rate).isNotNull();
        }
    }

    @Nested
    @DisplayName("Tests for method convertCurrency()")
    class ConvertCurrency {

        @Test
        void givenValidCurrencyConvertRequest_thenReturnConvertedCurrency() {
            // Given
            CurrencyConvertRequest currencyConvertRequest = CurrencyConvertRequest.builder()
                    .from(Currency.getInstance("USD"))
                    .to(Currency.getInstance("EUR"))
                    .amount(BigDecimal.valueOf(50))
                    .build();

            CurrencyConvert persistedCurrencyConvert = CurrencyConvert.builder()
                    .sourceCurrency(currencyConvertRequest.getFrom())
                    .targetCurrency(currencyConvertRequest.getTo())
                    .sourceAmount(currencyConvertRequest.getAmount())
                    .targetAmount(BigDecimal.valueOf(50))
                    .rate(BigDecimal.valueOf(1))
                    .build();

            when(fastForexClient.convert(any(), any(), any(), any())).thenReturn(mock(ClientCurrencyConvertResponse.class));
            when(currencyConvertRepository.save(any())).thenReturn(persistedCurrencyConvert);

            // When
            CurrencyConvertResponse actualResponse = exchangeService.convertCurrency(currencyConvertRequest);

            // Then
            assertThat(actualResponse).isNotNull();
            assertThat(actualResponse.getSourceCurrency()).isEqualTo(currencyConvertRequest.getFrom());
            assertThat(actualResponse.getTargetCurrency()).isEqualTo(currencyConvertRequest.getTo());
        }
    }

    @Nested
    @DisplayName("Tests for method findAllCurrencyConvert()")
    class FindAllCurrencyConvert {

        @Test
        void shouldReturnListOfCurrencyConvertResponses() {
            // Given
            List<CurrencyConvert> currencyConvertList = List.of(
                    new CurrencyConvert(),
                    new CurrencyConvert()
            );
            when(currencyConvertRepository.findAll()).thenReturn(currencyConvertList);

            // When
            List<CurrencyConvertResponse> actualResponseList = exchangeService.findAllCurrencyConvert();

            // Then
            assertThat(actualResponseList).isNotNull();
            assertThat(actualResponseList).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Tests for method findById()")
    class FindById {

        @Test
        void givenValidTransactionId_thenReturnsCurrencyConvertResponse() {
            // Given
            UUID validId = UUID.randomUUID();
            CurrencyConvert mockCurrencyConvert = new CurrencyConvert();
            when(currencyConvertRepository.findById(validId)).thenReturn(Optional.of(mockCurrencyConvert));

            // When
            CurrencyConvertResponse actualResponse = exchangeService.findById(validId);

            // Then
            assertThat(actualResponse).isNotNull();
        }

        @Test
        void givenInvalidTransactionId_thenThrowsExchangeDomainException() {
            // Given
            UUID invalidId = UUID.randomUUID();
            when(currencyConvertRepository.findById(invalidId)).thenReturn(Optional.empty());

            // When, Then
            assertThrows(ExchangeDomainException.class, () -> exchangeService.findById(invalidId));
        }
    }

    @Nested
    @DisplayName("Tests for method sortByField()")
    class SortByField {


        @Test
        void givenValidField_thenSortTheCurrencyTransactions() {

            // given
            String field = "id";
            List<CurrencyConvert> currencyConvertList = List.of(
                    new CurrencyConvert(),
                    new CurrencyConvert()
            );
            when(currencyConvertRepository.findAll(Sort.by(field))).thenReturn(currencyConvertList);

            // when
            List<CurrencyConvertResponse> responses = exchangeService.sortByField(field);

            // then
            assertThat(responses).isNotNull();
        }

    }

    @Nested
    @DisplayName("Tests for method findAll()")
    class FindAll {

        @Test
        void givenValidPageable_thenFindAll() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            List<CurrencyConvert> currencyConvertList = Arrays.asList(
                    new CurrencyConvert(),
                    new CurrencyConvert()
            );
            Page<CurrencyConvert> mockPage = new PageImpl<>(currencyConvertList, pageable, currencyConvertList.size());
            when(currencyConvertRepository.findAll(pageable)).thenReturn(mockPage);

            // when
            Page<CurrencyConvertResponse> result = exchangeService.findAll(pageable);

            // then
            assertThat(result).isNotNull();
        }

    }

}
