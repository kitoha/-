package com.example.wolbu.homwork.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoDto {
    private String name;
    private String email;
    private String phoneNumber;
}
