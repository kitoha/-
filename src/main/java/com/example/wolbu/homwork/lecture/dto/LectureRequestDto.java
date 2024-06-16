package com.example.wolbu.homwork.lecture.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LectureRequestDto {
    private String name;
    private int maximumStudent;
    private int price;
}
