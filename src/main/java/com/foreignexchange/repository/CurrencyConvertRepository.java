package com.foreignexchange.repository;

import com.foreignexchange.model.CurrencyConvert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyConvertRepository extends JpaRepository<CurrencyConvert, UUID> {

    Optional<CurrencyConvert> findById(UUID id);


}
