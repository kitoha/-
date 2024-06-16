package com.example.wolbu.homwork.lecture.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LectureRegistrationDto {
    private List<Long> lectureIds;
}
