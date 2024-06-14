package com.example.wolbu.homwork.lecture.repository;

import com.example.wolbu.homwork.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture,Long> {
    Lecture findByName(String name);
}
