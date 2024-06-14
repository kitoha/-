package com.example.wolbu.homwork.authority.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenInfo {
    private String grantType;
    private String accessToken;
}
