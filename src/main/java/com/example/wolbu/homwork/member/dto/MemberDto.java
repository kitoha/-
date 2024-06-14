package com.example.wolbu.homwork.member.dto;

import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.member.enums.MemberShip;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MemberDto {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private MemberShip memberShipType;
    private Set<Lecture> lectures;
}
