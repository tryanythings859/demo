package com.coindesk.demo.dao;

import com.coindesk.demo.dao.repository.CurrencyDetailRepository;
import com.coindesk.demo.dao.repository.CurrencyInfoRepository;
import com.coindesk.demo.dto.request.CurrencyInfoRequest;
import com.coindesk.demo.dto.request.UpdateCurrencyDetailRequest;
import com.coindesk.demo.dto.response.CurrencyDetailResponse;
import com.coindesk.demo.dto.mapper.CurrencyInfoEntityMapper;
import com.coindesk.demo.dto.mapper.CurrencyInfoRequestMapper;
import com.coindesk.demo.dto.response.CurrencyInfoResponse;
import com.coindesk.demo.entity.CurrencyDetailEntity;
import com.coindesk.demo.entity.CurrencyInfoEntity;
import com.coindesk.demo.entity.QCurrencyDetailEntity;
import com.coindesk.demo.entity.QCurrencyInfoEntity;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component("sqlDao")
public class SQLCurrencyDao implements CurrencyInfo {

    private final EntityManager entityManager;
    private final CurrencyInfoRepository currencyInfoRepo;
    private final CurrencyDetailRepository currencyDetailRepository;
    private final CurrencyInfoRequestMapper currencyInfoRequestMapper;
    private final CurrencyInfoEntityMapper currencyInfoEntityMapper;

    public SQLCurrencyDao(EntityManager entityManager,
                          CurrencyInfoRepository currencyInfoRepo,
                          CurrencyDetailRepository currencyDetailRepository,
                          CurrencyInfoRequestMapper currencyInfoRequestMapper,
                          CurrencyInfoEntityMapper currencyInfoEntityMapper) {
        this.entityManager = entityManager;
        this.currencyInfoRepo = currencyInfoRepo;
        this.currencyDetailRepository = currencyDetailRepository;
        this.currencyInfoRequestMapper = currencyInfoRequestMapper;
        this.currencyInfoEntityMapper = currencyInfoEntityMapper;
    }

    public CurrencyInfoResponse findAll() {
        QCurrencyInfoEntity qCurrencyInfo = QCurrencyInfoEntity.currencyInfoEntity;
        JPAQuery<CurrencyInfoEntity> query = new JPAQuery<>(entityManager);
        query.select(qCurrencyInfo)
                .from(qCurrencyInfo);
        CurrencyInfoEntity entity = query.fetchOne();
        return currencyInfoEntityMapper.toResponse(entity);
    }

    @Override
    public CurrencyDetailResponse findByCurrency(String currency) {
        QCurrencyDetailEntity qCurrencyDetail = QCurrencyDetailEntity.currencyDetailEntity;
        JPAQuery<CurrencyDetailEntity> query = new JPAQuery<>(entityManager);
        query.select(qCurrencyDetail)
                .from(qCurrencyDetail)
                .where(qCurrencyDetail.code.eq(currency));
        CurrencyDetailEntity entity = query.fetchOne();
        return currencyInfoEntityMapper.toDetailResponse(entity);
    }

    @Transactional
    @Override
    public CurrencyInfoResponse save(CurrencyInfoRequest request) {
        CurrencyInfoEntity entityMapper = currencyInfoRequestMapper.toEntity(request);
        CurrencyInfoEntity newEntity = currencyInfoRepo.save(entityMapper);
        return currencyInfoEntityMapper.toResponse(newEntity);
    }

    @Transactional
    @Override
    public void updateByCurrency(String currency, UpdateCurrencyDetailRequest request) {
        currencyDetailRepository.updateByCode(request.getSymbol(),
                request.getRate(),
                request.getRateFloat(),
                request.getDescription(),
                currency);
    }

    @Transactional
    @Override
    public void deleteByCurrency(String currency) {
        currencyDetailRepository.deleteByCode(currency);
    }
}
