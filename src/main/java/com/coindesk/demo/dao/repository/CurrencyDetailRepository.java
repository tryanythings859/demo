package com.coindesk.demo.dao.repository;

import com.coindesk.demo.entity.CurrencyDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CurrencyDetailRepository extends JpaRepository<CurrencyDetailEntity, Long> {

    @Modifying
    @Query("UPDATE CurrencyDetailEntity c SET c.symbol = :symbol, c.rate = :rate, c.rateFloat = :rateFloat, c.description = :description WHERE c.code = :code")
    int updateByCode(@Param("symbol") String symbol, @Param("rate") String rate, @Param("rateFloat") BigDecimal rateFloat, @Param("description") String description, @Param("code") String code);

    void deleteByCode(String delete);
}
