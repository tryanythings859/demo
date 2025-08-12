package com.coindesk.demo.dto;

import com.coindesk.demo.dto.response.ErrorResult;

public interface ErrorResultProxy {
    default <T> ErrorResult<T> newCreated(T response) {
        return ErrorResult.newCreated(response);
    }
}
