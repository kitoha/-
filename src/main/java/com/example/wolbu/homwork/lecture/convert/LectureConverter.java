package com.example.wolbu.homwork.lecture.convert;

import com.example.wolbu.homwork.lecture.dto.LectureInfoDto;
import com.example.wolbu.homwork.lecture.entity.Lecture;

public class LectureConverter {

    public static LectureInfoDto convertToLectureInfo(Lecture lecture) {
        return LectureInfoDto.builder()
                .lectureId(lecture.getId())
                .name(lecture.getName())
                .teacherName(lecture.getTeacher().getName())
                .currentStudent(lecture.getApplicantsCount())
                .maximumStudent(lecture.getMaximumStudent())
                .price(lecture.getPrice())
                .build();
    }
}
