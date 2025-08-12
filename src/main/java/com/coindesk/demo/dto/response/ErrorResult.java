package com.coindesk.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorResult<T> {
    private boolean success;
    private T errorMessage;

    public static <T> ErrorResult<T> newCreated(T errorMessage) {
        return ErrorResult.<T>builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }
}
