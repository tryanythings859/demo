package com.coindesk.demo.dao.repository;

import com.coindesk.demo.entity.CurrencyInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyInfoRepository extends JpaRepository<CurrencyInfoEntity, Long> {
}
