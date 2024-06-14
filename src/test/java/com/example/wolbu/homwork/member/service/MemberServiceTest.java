package com.example.wolbu.homwork.member.service;

import com.example.wolbu.homwork.authority.dto.TokenInfo;
import com.example.wolbu.homwork.authority.provider.JwtTokenProvider;
import com.example.wolbu.homwork.common.dto.BaseResponse;
import com.example.wolbu.homwork.common.enums.ResultCode;
import com.example.wolbu.homwork.member.converter.MemberConverter;
import com.example.wolbu.homwork.member.dto.LoginDto;
import com.example.wolbu.homwork.member.dto.MemberDto;
import com.example.wolbu.homwork.member.dto.MemberInfoDto;
import com.example.wolbu.homwork.member.entity.Member;
import com.example.wolbu.homwork.member.enums.MemberShip;
import com.example.wolbu.homwork.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private MemberService memberService;

    @Test
    public void signup_ShouldReturnSuccess_WhenMemberIsNew() {
        // given
        MemberDto memberDto = new MemberDto();
        memberDto.setName("username");
        memberDto.setPassword("password");
        memberDto.setEmail("email@example.com");
        memberDto.setPhoneNumber("1234567890");
        memberDto.setMemberShipType(MemberShip.STUDENT);
        when(memberRepository.findByName(memberDto.getName())).thenReturn(null);
        Member savedMember = Member.builder()
                .id(1L)
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .phoneNumber(memberDto.getPhoneNumber())
                .memberShip(memberDto.getMemberShipType())
                .build();
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberInfoDto expectedMemberInfoDto = MemberConverter.convertToMemberDto(savedMember);

        // when
        BaseResponse<MemberInfoDto> response = memberService.signup(memberDto);

        // then
        assertEquals(ResultCode.SUCCESS.getStatusCode(), response.getResultCode());
        assertEquals(expectedMemberInfoDto.getName(), response.getData().getName());
        verify(memberRepository).findByName(memberDto.getName());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    public void signup_ShouldReturnError_WhenMemberAlreadyExists() {
        // given
        MemberDto memberDto = new MemberDto();
        memberDto.setName("username");
        memberDto.setPassword("password");
        memberDto.setEmail("email@example.com");
        memberDto.setPhoneNumber("1234567890");
        memberDto.setMemberShipType(MemberShip.STUDENT);
        Member existingMember = Member.builder()
                .id(1L)
                .name(memberDto.getName())
                .password(memberDto.getPassword())
                .email(memberDto.getEmail())
                .phoneNumber(memberDto.getPhoneNumber())
                .memberShip(memberDto.getMemberShipType())
                .build();
        when(memberRepository.findByName(memberDto.getName())).thenReturn(existingMember);

        // when
        BaseResponse<MemberInfoDto> response = memberService.signup(memberDto);

        // then
        assertEquals(ResultCode.ERROR.getStatusCode(), response.getResultCode());
        assertEquals("이미 등록된 ID 입니다.", response.getMessage());
        verify(memberRepository).findByName(memberDto.getName());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    public void login_ReturnTokenInfo_WhenAuthenticationIsSuccessful() {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setName("testUser");
        loginDto.setPassword("12345");
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        TokenInfo expectedTokenInfo = new TokenInfo("token", "refreshToken");
        when(jwtTokenProvider.createToken(authentication)).thenReturn(expectedTokenInfo);

        // when
        TokenInfo actualTokenInfo = memberService.login(loginDto);

        // then
        assertEquals(expectedTokenInfo, actualTokenInfo);
        verify(authenticationManagerBuilder).getObject();
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).createToken(authentication);
    }
}