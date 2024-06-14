package com.example.wolbu.homwork.lecture.controller;

import com.example.wolbu.homwork.common.dto.BaseResponse;
import com.example.wolbu.homwork.common.enums.ResultCode;
import com.example.wolbu.homwork.lecture.dto.EnrollmentResponseDto;
import com.example.wolbu.homwork.lecture.dto.LectureRegistrationDto;
import com.example.wolbu.homwork.lecture.dto.LectureRequestDto;
import com.example.wolbu.homwork.lecture.dto.LectureInfoDto;
import com.example.wolbu.homwork.lecture.enums.SortedStatus;
import com.example.wolbu.homwork.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LectureController {

    private final LectureService lectureService;

    @PostMapping("/v1/lecture")
    public BaseResponse<LectureInfoDto> createLecture(@RequestBody LectureRequestDto lectureRequestDto) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = principal.getUsername();
        try {
            return lectureService.createLecture(lectureRequestDto, name);
        } catch (Exception e) {
            log.error("강의 등록 중 오류가 발생하였습니다.", e);
            return new BaseResponse<>(ResultCode.ERROR, null, "강의 등록 중 오류가 발생하였습니다.");
        }
    }

    @PostMapping("/v1/lecture/enroll")
    public BaseResponse<List<EnrollmentResponseDto>> registerLecture(@RequestBody LectureRegistrationDto lectureRegistrationDto) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = principal.getUsername();
        try {
            return lectureService.registerLecture(lectureRegistrationDto, name);
        } catch (Exception e) {
            log.error("강의 신청 중 오류가 발생하였습니다.", e);
            return new BaseResponse<>(ResultCode.ERROR, null, "강의 신청 중 오류가 발생하였습니다.");
        }
    }

    @GetMapping("/v1/lecture")
    public BaseResponse<List<LectureInfoDto>> getLectures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "LATEST") SortedStatus sortedStatus) {
        try {
            return lectureService.getLectureInfo(page, size, sortedStatus);
        } catch (Exception e) {
            log.error("강의 조회 중 오류가 발생하였습니다.", e);
            return new BaseResponse<>(ResultCode.ERROR, null, "강의 조회 중 오류가 발생하였습니다.");
        }
    }
}
