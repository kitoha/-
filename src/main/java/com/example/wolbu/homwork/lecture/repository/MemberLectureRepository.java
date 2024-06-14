package com.example.wolbu.homwork.lecture.repository;

import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.lecture.entity.LectureMember;
import com.example.wolbu.homwork.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLectureRepository extends JpaRepository<LectureMember,Long> {
    long countByLecture(Lecture lecture);
    boolean existsByMemberAndLecture(Member member, Lecture lecture);
}
