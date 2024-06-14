package com.example.wolbu.homwork.member.converter;

import com.example.wolbu.homwork.member.dto.MemberInfoDto;
import com.example.wolbu.homwork.member.entity.Member;

public class MemberConverter {
    public static MemberInfoDto convertToMemberDto(Member member){
        return MemberInfoDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
