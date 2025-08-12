package com.coindesk.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.Instant;

@Data
@Embeddable
public class AuditTimestamps {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @Column(name = "updated_at", nullable = false, columnDefinition = "datetime(3)")
    @LastModifiedDate
    protected Instant updatedAt;
}
