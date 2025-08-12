package com.coindesk.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Result<T> {
    private boolean success;
    private T result;

    public static <T> Result<T> newCreated(T result) {
        return Result.<T>builder()
                .success(true)
                .result(result)
                .build();
    }
}
