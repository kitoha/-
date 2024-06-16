package com.example.wolbu.homwork.lecture.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;

import com.example.wolbu.homwork.common.dto.BaseResponse;
import com.example.wolbu.homwork.common.enums.ResultCode;
import com.example.wolbu.homwork.lecture.convert.LectureConverter;
import com.example.wolbu.homwork.lecture.dto.EnrollmentResponseDto;
import com.example.wolbu.homwork.lecture.dto.LectureInfoDto;
import com.example.wolbu.homwork.lecture.dto.LectureRegistrationDto;
import com.example.wolbu.homwork.lecture.dto.LectureRequestDto;
import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.lecture.entity.LectureMember;
import com.example.wolbu.homwork.lecture.enums.SortedStatus;
import com.example.wolbu.homwork.lecture.repository.LectureRepository;
import com.example.wolbu.homwork.lecture.repository.MemberLectureRepository;
import com.example.wolbu.homwork.member.entity.Member;
import com.example.wolbu.homwork.member.enums.MemberShip;
import com.example.wolbu.homwork.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private MemberLectureRepository memberLectureRepository;

    @Mock
    private LectureSorterService lectureSorterService;

    @InjectMocks
    private LectureService lectureService;

    private Member teacher;
    private Member student;
    private LectureRequestDto lectureRequestDto;
    private Lecture lecture;
    private LectureRegistrationDto lectureRegistrationDto;

    @BeforeEach
    public void setUp() {
        teacher = new Member();
        teacher.setName("teacher");
        teacher.setMemberShip(MemberShip.TEACHER);

        student = new Member();
        student.setName("student");
        student.setMemberShip(MemberShip.STUDENT);

        lectureRequestDto = new LectureRequestDto();
        lectureRequestDto.setName("Test Lecture");
        lectureRequestDto.setMaximumStudent(30);
        lectureRequestDto.setPrice(100000);

        lecture = new Lecture();
        lecture.setId(1L);
        lecture.setName("Test Lecture");
        lecture.setTeacher(teacher);
        lecture.setMaximumStudent(30);
        lecture.setPrice(100000);

        lectureRegistrationDto = new LectureRegistrationDto();
        lectureRegistrationDto.setLectureIds(List.of(1L));
    }

    @Test
    public void createLecture_ShouldReturnSuccess_WhenValidRequest() {
        // given
        when(memberService.getMemberInfoByName("teacher")).thenReturn(teacher);
        when(lectureRepository.findByName("Test Lecture")).thenReturn(null);
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        // when
        BaseResponse<LectureInfoDto> response = lectureService.createLecture(lectureRequestDto, "teacher");

        // then
        assertEquals(ResultCode.SUCCESS.getStatusCode(), response.getResultCode());
        assertNotNull(response.getData());
        assertEquals("강의가 정상적으로 등록되었습니다.", response.getMessage());
        verify(memberService).getMemberInfoByName("teacher");
        verify(lectureRepository).findByName("Test Lecture");
        verify(lectureRepository).save(any(Lecture.class));
    }

    @Test
    public void createLecture_ShouldReturnError_WhenStudentTriesToCreateLecture() {
        // given
        when(memberService.getMemberInfoByName("student")).thenReturn(student);

        // when
        BaseResponse<LectureInfoDto> response = lectureService.createLecture(lectureRequestDto, "student");

        // then
        assertEquals(ResultCode.ERROR, response.getResultCode());
        assertNull(response.getData());
        assertEquals("학생은 강의를 등록할 수 없습니다.", response.getMessage());
        verify(memberService).getMemberInfoByName("student");
        verify(lectureRepository, never()).findByName(anyString());
        verify(lectureRepository, never()).save(any(Lecture.class));
    }

    @Test
    public void createLecture_ShouldReturnError_WhenLectureAlreadyExists() {
        // given
        when(memberService.getMemberInfoByName("teacher")).thenReturn(teacher);
        when(lectureRepository.findByName("Test Lecture")).thenReturn(lecture);

        // when
        BaseResponse<LectureInfoDto> response = lectureService.createLecture(lectureRequestDto, "teacher");

        // then
        assertEquals(ResultCode.ERROR.getStatusCode(), response.getResultCode());
        assertNull(response.getData());
        assertEquals("동일한 이름의 강의가 이미 등록되어있습니다.", response.getMessage());
        verify(memberService).getMemberInfoByName("teacher");
        verify(lectureRepository).findByName("Test Lecture");
        verify(lectureRepository, never()).save(any(Lecture.class));
    }

    @Test
    public void registerLecture_ShouldReturnSuccess_WhenValidRequest() {
        // given
        when(memberService.getMemberInfoByName("student")).thenReturn(student);
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
        when(memberLectureRepository.countByLecture(lecture)).thenReturn(0L);
        when(memberLectureRepository.existsByMemberAndLecture(student, lecture)).thenReturn(false);
        LectureMember lectureMember = new LectureMember();
        lectureMember.setLecture(lecture);
        when(memberLectureRepository.save(any(LectureMember.class))).thenReturn(lectureMember);

        // when
        BaseResponse<List<EnrollmentResponseDto>> response = lectureService.registerLecture(lectureRegistrationDto, "student");

        // then
        assertEquals(ResultCode.SUCCESS.getStatusCode(), response.getResultCode());
        assertEquals("강의 등록이 완료되었습니다.", response.getMessage());
        verify(memberService).getMemberInfoByName("student");
        verify(lectureRepository, times(1)).findById(1L);
        verify(memberLectureRepository, times(1)).countByLecture(lecture);
        verify(memberLectureRepository, times(1)).existsByMemberAndLecture(student, lecture);
        verify(memberLectureRepository, times(1)).save(any(LectureMember.class));
    }

    @Test
    public void getLectureInfo_ShouldReturnSortedLectures() {
        // given
        Page<Lecture> lecturePage = new PageImpl<>(List.of(lecture));
        List<LectureInfoDto> sortedLectures = List.of(LectureConverter.convertToLectureInfo(lecture));
        when(lectureRepository.findAll(PageRequest.of(0, 10))).thenReturn(lecturePage);
        when(lectureSorterService.sort(lecturePage, SortedStatus.LATEST)).thenReturn(sortedLectures);

        // when
        BaseResponse<List<LectureInfoDto>> response = lectureService.getLectureInfo(0, 10, SortedStatus.LATEST);

        // then
        assertEquals(ResultCode.SUCCESS.getStatusCode(), response.getResultCode());
        assertIterableEquals(sortedLectures, response.getData());
        verify(lectureRepository).findAll(PageRequest.of(0, 10));
        verify(lectureSorterService).sort(lecturePage, SortedStatus.LATEST);
    }
}
