package com.example.wolbu.homwork.member.dto;

import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.member.enums.MemberShip;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class MemberDto {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private MemberShip memberShipType;
    private Set<Lecture> lectures;
}
