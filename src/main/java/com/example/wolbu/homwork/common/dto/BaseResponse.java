package com.example.wolbu.homwork.common.dto;

import com.example.wolbu.homwork.common.enums.ResultCode;
import lombok.Getter;

@Getter
public class BaseResponse<T> {
    private String resultCode;
    private T data;
    private String message;

    public BaseResponse(ResultCode resultCode, T data, String message) {
        this.resultCode = resultCode.getStatusCode();
        this.data = data;
        this.message = message;
    }
}
