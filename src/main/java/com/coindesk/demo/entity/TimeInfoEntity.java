package com.coindesk.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "country_time_info")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TimeInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_iso")
    private Instant updatedISO;

    @Column(name = "updated_uk")
    private Instant updatedUK;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_info_id", nullable = false)
    private CurrencyInfoEntity currencyInfo;

    public void setCurrencyInfo(CurrencyInfoEntity currencyInfo) {
        this.currencyInfo = currencyInfo;
    }
}
