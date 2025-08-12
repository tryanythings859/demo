package com.coindesk.demo.dto;

import com.coindesk.demo.dto.response.Result;

public interface ResultProxy {
    default <T> Result<T> newCreated(T response) {
        return Result.newCreated(response);
    }
}
