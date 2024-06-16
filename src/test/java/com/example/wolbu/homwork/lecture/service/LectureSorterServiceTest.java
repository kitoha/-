package com.example.wolbu.homwork.lecture.service;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.example.wolbu.homwork.lecture.convert.LectureConverter;
import com.example.wolbu.homwork.lecture.dto.LectureInfoDto;
import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.lecture.enums.SortedStatus;
import com.example.wolbu.homwork.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
public class LectureSorterServiceTest {

    @InjectMocks
    private LectureSorterService lectureSorterService;

    private List<Lecture> lectures;
    private Page<Lecture> lecturePage;

    @BeforeEach
    public void setUp() {
        Member member = new Member();
        member.setName("teacher");
        lectures = new ArrayList<>();
        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setName("Lecture 1");
        lecture.setPrice(1000);
        lecture.setTeacher(member);
        lectures.add(lecture);
        Lecture lecture2 = new Lecture();
        lecture2.setId(2L);
        lecture2.setName("Lecture 2");
        lecture2.setTeacher(member);
        lecture2.setPrice(1000);
        lectures.add(lecture2);
        Lecture lecture3 = new Lecture();
        lecture3.setId(3L);
        lecture3.setName("Lecture 3");
        lecture3.setTeacher(member);
        lecture3.setPrice(1000);
        lectures.add(lecture3);

        lecturePage = new PageImpl<>(lectures);
    }

    @Test
    public void testSortByLatest() {
        // given
        List<LectureInfoDto> expected = lectures.stream()
                .sorted(Comparator.comparingLong(Lecture::getId))
                .map(LectureConverter::convertToLectureInfo)
                .collect(Collectors.toList());

        // when
        List<LectureInfoDto> actual = lectureSorterService.sort(lecturePage, SortedStatus.LATEST);

        // then
        assertEquals(expected.get(0).getLectureId(), actual.get(0).getLectureId());
    }

    @Test
    public void testSortByApplicants() {
        // given
        List<LectureInfoDto> expected = lectures.stream()
                .sorted(Comparator.comparingInt(Lecture::getApplicantsCount).reversed())
                .map(LectureConverter::convertToLectureInfo)
                .collect(Collectors.toList());

        // when
        List<LectureInfoDto> actual = lectureSorterService.sort(lecturePage, SortedStatus.APPLICANTS);

        // then
        assertEquals(expected.get(0).getLectureId(), actual.get(0).getLectureId());
        assertEquals(expected.get(1).getLectureId(), actual.get(1).getLectureId());
        assertEquals(expected.get(2).getLectureId(), actual.get(2).getLectureId());
    }

    @Test
    public void testSortByApplicationRate() {
        // given
        List<LectureInfoDto> expected = lectures.stream()
                .sorted(Comparator.comparingDouble(Lecture::getApplicationRate).reversed())
                .map(LectureConverter::convertToLectureInfo)
                .collect(Collectors.toList());

        // when
        List<LectureInfoDto> actual = lectureSorterService.sort(lecturePage, SortedStatus.APPLICATION_RATE);

        // then
        assertEquals(expected.get(0).getLectureId(), actual.get(0).getLectureId());
        assertEquals(expected.get(1).getLectureId(), actual.get(1).getLectureId());
        assertEquals(expected.get(2).getLectureId(), actual.get(2).getLectureId());
    }
}