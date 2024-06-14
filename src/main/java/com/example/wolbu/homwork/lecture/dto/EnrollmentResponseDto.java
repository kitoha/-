package com.example.wolbu.homwork.lecture.dto;

import com.example.wolbu.homwork.lecture.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnrollmentResponseDto {
    LectureInfoDto lectureInfoDto;
    EnrollmentStatus enrollmentStatus;
}
