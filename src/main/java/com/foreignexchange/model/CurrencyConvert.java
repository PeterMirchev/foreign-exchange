package com.foreignexchange.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConvert {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private Currency sourceCurrency;
    @Column(nullable = false)
    private Currency targetCurrency;
    @Column(nullable = false)
    private BigDecimal sourceAmount;
    @Column(nullable = false)
    private BigDecimal targetAmount;
    @Column(nullable = false)
    private BigDecimal rate;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedOn;
}
