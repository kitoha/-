package com.example.wolbu.homwork.lecture.service;

import com.example.wolbu.homwork.lecture.convert.LectureConverter;
import com.example.wolbu.homwork.lecture.dto.LectureInfoDto;
import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.lecture.enums.SortedStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureSorterService {
    public List<LectureInfoDto> sort(Page<Lecture> lecturePage, SortedStatus sortedStatus) {
        return switch (sortedStatus) {
            case LATEST -> getLectureSortedByDesc(lecturePage);
            case APPLICANTS -> getLectureSortedByStudentSize(lecturePage);
            case APPLICATION_RATE -> getLectureSortedByStudentSizeRate(lecturePage);
        };
    }

    private List<LectureInfoDto> getLectureSortedByDesc(Page<Lecture> lecturePage) {
        return lecturePage.stream()
                .sorted(Comparator.comparingLong(Lecture::getId))
                .map(LectureConverter::convertToLectureInfo)
                .collect(Collectors.toList());
    }

    private List<LectureInfoDto> getLectureSortedByStudentSize(Page<Lecture> lecturePage) {
        return lecturePage.stream()
                .sorted(Comparator.comparingInt(Lecture::getApplicantsCount).reversed())
                .map(LectureConverter::convertToLectureInfo)
                .collect(Collectors.toList());
    }

    private List<LectureInfoDto> getLectureSortedByStudentSizeRate(Page<Lecture> lecturePage) {
        return lecturePage.stream()
                .sorted(Comparator.comparingDouble(Lecture::getApplicationRate).reversed())
                .map(LectureConverter::convertToLectureInfo)
                .collect(Collectors.toList());
    }
}
