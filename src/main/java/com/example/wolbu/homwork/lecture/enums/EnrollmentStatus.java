package com.example.wolbu.homwork.lecture.enums;

import lombok.Getter;

@Getter
public enum EnrollmentStatus {
    ENROLLMENT_FULL("강의 인원 초과"),
    ALREADY_ENROLLED("해당 강의 이미 수강 중"),
    ENROLLMENT_COMPLETED("등록 완료");

    private final String description;

    EnrollmentStatus(String description) {
        this.description = description;
    }
}
