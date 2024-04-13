package com.foreignexchange.api.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
public class CurrencyConvertRequest {

    @NotNull(message = "Currency cannot be null")
    private Currency from;
    @NotNull(message = "Currency cannot be null")
    private Currency to;
    @NotNull
    @Positive(message = "amount must be positive")
    private BigDecimal amount;
    @AssertTrue(message = "\"from\" and \"to\" must be different currencies.")
    public boolean isAssertTrue() {
        return !this.from.equals(this.to);
    }
}
