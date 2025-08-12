package com.coindesk.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "currency_detail",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_info_id_price_detail_code", columnNames = {"currency_info_id", "code"})
        }
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CurrencyDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "rate")
    private String rate;

    @Column(name = "description")
    private String description;

    @Column(name = "rate_float", columnDefinition = "Decimal(11,4)")
    private BigDecimal rateFloat;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_info_id", nullable = false)
    private CurrencyInfoEntity currencyInfo;

    public void setInfo(CurrencyInfoEntity currencyInfo) {
        this.currencyInfo = currencyInfo;
    }
}
