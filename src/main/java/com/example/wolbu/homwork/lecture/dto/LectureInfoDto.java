package com.example.wolbu.homwork.lecture.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureInfoDto {
    private Long lectureId;
    private String name;
    private String teacherName;
    private int currentStudent;
    private int maximumStudent;
    private int price;
}
