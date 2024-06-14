package com.example.wolbu.homwork.common.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS("200"),
    ERROR("500");

    private String statusCode;

    ResultCode(String errorCode) {
        this.statusCode = errorCode;
    }
}
