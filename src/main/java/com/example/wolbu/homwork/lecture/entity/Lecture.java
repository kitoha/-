package com.example.wolbu.homwork.lecture.entity;

import com.example.wolbu.homwork.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private int maximumStudent;
    @Column
    private int price;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Member teacher;

    @OneToMany(mappedBy = "lecture")
    private List<LectureMember> students = new ArrayList<>();

    public int getApplicantsCount() {
        return students != null ? students.size() : 0;
    }

    public double getApplicationRate() {
        return maximumStudent > 0 ? (double) getApplicantsCount() / maximumStudent : 0;
    }
}
