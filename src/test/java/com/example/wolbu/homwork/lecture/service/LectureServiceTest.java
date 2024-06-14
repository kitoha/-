package com.example.wolbu.homwork.lecture.service;

import com.example.wolbu.homwork.lecture.dto.EnrollmentResponseDto;
import com.example.wolbu.homwork.lecture.entity.Lecture;
import com.example.wolbu.homwork.lecture.repository.LectureRepository;
import com.example.wolbu.homwork.lecture.repository.MemberLectureRepository;
import com.example.wolbu.homwork.member.dto.MemberDto;
import com.example.wolbu.homwork.member.entity.Member;
import com.example.wolbu.homwork.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class LectureServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private MemberLectureRepository memberLectureRepository;
    @Autowired
    private LectureService lectureService;

    private List<Member> members;

    @BeforeEach
    public void before(){
        MemberDto teacher = new MemberDto();
        teacher.setName("teacher");

        memberService.signup(teacher);
        Member member = memberService.getMemberInfoByName("teacher");
        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setName("test");
        lecture.setTeacher(member);
        lecture.setMaximumStudent(10);
        lecture.setPrice(100000);
        Lecture savedlecture =lectureRepository.save(lecture);

        members = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            MemberDto memberDto = new MemberDto();
            memberDto.setName("test" + i);
            memberService.signup(memberDto);
            members.add(memberService.getMemberInfoByName("test" + i));
        }
    }

    @AfterEach
    public void after(){
        lectureRepository.deleteAll();
    }


    @Test
    @Transactional
    void concurrency_test() throws InterruptedException {
        var count = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CountDownLatch countDownLatch = new CountDownLatch(count);

        for(int i = 0; i < count; i++) {
            int index = i;
            executorService.execute(() -> {
                Member member = members.get(index);
                lectureService.enrollment(1L,member);

                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        Lecture lecture = lectureRepository.findById(1L).orElseThrow();

        assertEquals(3,lecture.getApplicantsCount());
    }
}