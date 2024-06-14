package com.example.wolbu.homwork.member.service;

import com.example.wolbu.homwork.authority.provider.JwtTokenProvider;
import com.example.wolbu.homwork.authority.dto.TokenInfo;
import com.example.wolbu.homwork.common.dto.BaseResponse;
import com.example.wolbu.homwork.common.enums.ResultCode;
import com.example.wolbu.homwork.member.converter.MemberConverter;
import com.example.wolbu.homwork.member.dto.LoginDto;
import com.example.wolbu.homwork.member.dto.MemberDto;
import com.example.wolbu.homwork.member.dto.MemberInfoDto;
import com.example.wolbu.homwork.member.entity.Member;
import com.example.wolbu.homwork.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public BaseResponse<MemberInfoDto> signup(MemberDto memberDto) {
        Member memberEntity = memberRepository.findByName(memberDto.getName());

        if (memberEntity != null) {
            return new BaseResponse<>(ResultCode.ERROR, null, "이미 등록된 ID 입니다.");
        }

        Member newMember = Member.builder()
                .id(null)
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .phoneNumber(memberDto.getPhoneNumber())
                .memberShip(memberDto.getMemberShipType())
                .build();

        Member savedMember = memberRepository.save(newMember);
        MemberInfoDto memberInfoDto = MemberConverter.convertToMemberDto(savedMember);

        return new BaseResponse<>(ResultCode.SUCCESS, memberInfoDto, "회원 가입 완료되었습니다.");
    }

    public TokenInfo login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getName(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        return jwtTokenProvider.createToken(authentication);
    }

    public Member getMemberInfoByName(String name) {
        return memberRepository.findByName(name);
    }
}
