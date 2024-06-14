package com.example.wolbu.homwork.lecture.service;

import com.example.wolbu.homwork.common.dto.BaseResponse;
import com.example.wolbu.homwork.common.enums.ResultCode;
import com.example.wolbu.homwork.lecture.convert.LectureConverter;
import com.example.wolbu.homwork.lecture.dto.EnrollmentResponseDto;
import com.example.wolbu.homwork.lecture.dto.LectureRegistrationDto;
import com.example.wolbu.homwork.lecture.dto.LectureRequestDto;
import com.example.wolbu.homwork.lecture.dto.LectureInfoDto;
import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.lecture.entity.LectureMember;
import com.example.wolbu.homwork.lecture.enums.EnrollmentStatus;
import com.example.wolbu.homwork.lecture.enums.SortedStatus;
import com.example.wolbu.homwork.lecture.repository.LectureRepository;
import com.example.wolbu.homwork.lecture.repository.MemberLectureRepository;
import com.example.wolbu.homwork.member.enums.MemberShip;
import com.example.wolbu.homwork.member.entity.Member;
import com.example.wolbu.homwork.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LectureService {

    private final MemberService memberService;
    private final LectureRepository lectureRepository;
    private final MemberLectureRepository memberLectureRepository;

    @Transactional
    public synchronized BaseResponse<LectureInfoDto> createLecture(LectureRequestDto lectureRequestDto, String name) {
        Member member = memberService.getMemberInfoByName(name);
        if (isStudent(member.getMemberShip())) {
            return new BaseResponse<>(ResultCode.ERROR, null, "학생은 강의를 등록할 수 없습니다.");
        }

        Lecture currentLecture = lectureRepository.findByName(lectureRequestDto.getName());

        if (currentLecture != null) {
            return new BaseResponse<>(ResultCode.ERROR, null, "동일한 이름의 강의가 이미 등록되어있습니다.");
        }

        Lecture lecture = new Lecture();
        lecture.setName(lectureRequestDto.getName());
        lecture.setTeacher(member);
        lecture.setMaximumStudent(lectureRequestDto.getMaximumStudent());
        lecture.setPrice(lectureRequestDto.getPrice());

        Lecture savedLecture = lectureRepository.save(lecture);
        LectureInfoDto savedLectureDto = LectureConverter.convertToLectureInfo(savedLecture);

        return new BaseResponse<>(ResultCode.SUCCESS, savedLectureDto, "강의가 정상적으로 등록되었습니다.");
    }

    public BaseResponse<List<EnrollmentResponseDto>> registerLecture(LectureRegistrationDto lectureRegistrationDto, String name) {
        Member member = memberService.getMemberInfoByName(name);
        List<EnrollmentResponseDto> enrollmentResponseDtoList = new ArrayList<>();

        for (Long lectureId : lectureRegistrationDto.getLectureIds()) {
            EnrollmentResponseDto enrollmentResponseDto = enrollment(lectureId, member);
            enrollmentResponseDtoList.add(enrollmentResponseDto);
        }

        return new BaseResponse<>(ResultCode.SUCCESS, enrollmentResponseDtoList, "강의 등록이 완료되었습니다.");
    }

    @Transactional
    public synchronized EnrollmentResponseDto enrollment(Long lectureId, Member member) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("강의가 없습니다."));

        LectureInfoDto lectureInfoDto = LectureConverter.convertToLectureInfo(lecture);

        if (lecture.getMaximumStudent() <= memberLectureRepository.countByLecture(lecture)) {
            return new EnrollmentResponseDto(lectureInfoDto, EnrollmentStatus.ENROLLMENT_FULL);
        }

        if (memberLectureRepository.existsByMemberAndLecture(member, lecture)) {
            return new EnrollmentResponseDto(lectureInfoDto, EnrollmentStatus.ALREADY_ENROLLED);
        }

        LectureMember lectureMember = new LectureMember();
        lectureMember.setMember(member);
        lectureMember.setLecture(lecture);
        LectureMember savedLectureMember = memberLectureRepository.save(lectureMember);
        LectureInfoDto savedLectureInfoDto = LectureConverter.convertToLectureInfo(savedLectureMember.getLecture());
        return new EnrollmentResponseDto(savedLectureInfoDto, EnrollmentStatus.ENROLLMENT_COMPLETED);
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<LectureInfoDto>> getLectureInfo(int page, int size, SortedStatus sortedStatus) {
        Page<Lecture> lecturePage = lectureRepository.findAll(PageRequest.of(page, size));

        return switch (sortedStatus) {
            case LATEST -> new BaseResponse<>(ResultCode.SUCCESS, getLectureSortedByDesc(lecturePage), "최근 등록순");
            case APPLICANTS ->
                    new BaseResponse<>(ResultCode.SUCCESS, getLectureSortedByStudentSize(lecturePage), "신청자 많은 순");
            case APPLICATION_RATE ->
                    new BaseResponse<>(ResultCode.SUCCESS, getLectureSortedByStudentSizeRate(lecturePage), "신청률 높은 순");
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

    private boolean isStudent(MemberShip memberShip) {
        return MemberShip.STUDENT.equals(memberShip);
    }
}
