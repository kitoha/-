package com.example.wolbu.homwork.member.controller;

import com.example.wolbu.homwork.authority.dto.TokenInfo;
import com.example.wolbu.homwork.common.dto.BaseResponse;
import com.example.wolbu.homwork.common.enums.ResultCode;
import com.example.wolbu.homwork.member.dto.LoginDto;
import com.example.wolbu.homwork.member.dto.MemberDto;
import com.example.wolbu.homwork.member.dto.MemberInfoDto;
import com.example.wolbu.homwork.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/v1/signup")
    public BaseResponse<MemberInfoDto> signUp(@RequestBody MemberDto memberDto) {
        try {
            return memberService.signup(memberDto);
        } catch (Exception e) {
            log.error("회원 가입 중 오류가 발생하였습니다.", e);
            return new BaseResponse<>(ResultCode.ERROR, null, "회원 가입 중 오류가 발생하였습니다.");
        }
    }

    @PostMapping("/v1/login")
    public BaseResponse<TokenInfo> login(@RequestBody LoginDto loginDto) {
        try {
            TokenInfo tokenInfo = memberService.login(loginDto);
            return new BaseResponse<>(ResultCode.SUCCESS, tokenInfo, "로그인이 성공하였습니다.");
        } catch (Exception e) {
            log.error("로그인 중 오류가 발생하였습니다", e);
            return new BaseResponse<>(ResultCode.ERROR, null, "로그인 중 오류가 발생하였습니다.");
        }
    }
}
