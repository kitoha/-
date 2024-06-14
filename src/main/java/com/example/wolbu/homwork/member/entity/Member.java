package com.example.wolbu.homwork.member.entity;


import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.lecture.entity.LectureMember;
import com.example.wolbu.homwork.member.enums.MemberShip;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberShip memberShip;

    @OneToMany(mappedBy = "teacher")
    private Set<Lecture> taughtLectures;

    @OneToMany(mappedBy = "member")
    private Set<LectureMember> memberLectures;
}
