package com.coindesk.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "currency_info")
@EntityListeners(AuditingEntityListener.class) // 關鍵
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CurrencyInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "disclaimer")
    private String disclaimer;

    @Column(name = "chart_name")
    private String chartName;

    @OneToOne(mappedBy = "currencyInfo", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private TimeInfoEntity timeInfo;

    @Embedded
    private AuditTimestamps auditTimestamps = new AuditTimestamps();

    @OneToMany(
            mappedBy = "currencyInfo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @MapKey(name = "code")
    private Map<String, CurrencyDetailEntity> currencies = new HashMap<>();

    public void putCurrency(CurrencyDetailEntity mapping) {
        mapping.setInfo(this);
        this.currencies.put(mapping.getCode(), mapping);
    }

    public void removeCurrency(String currencyCode) {
        CurrencyDetailEntity m = this.currencies.remove(currencyCode);
        if (m != null) m.setInfo(null);
    }

    public void setTimeInfo(TimeInfoEntity timeInfo) {
        this.timeInfo = timeInfo;
        timeInfo.setCurrencyInfo(this);
    }

    public void removeTimeInfo() {
        this.timeInfo = null;
    }

}
