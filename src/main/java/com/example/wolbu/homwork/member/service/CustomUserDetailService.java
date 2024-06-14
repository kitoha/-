package com.example.wolbu.homwork.member.service;

import com.example.wolbu.homwork.member.entity.Member;
import com.example.wolbu.homwork.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(username);

        if (member != null) {
            return createUserDetails(member);
        }

        throw new UsernameNotFoundException("해당 유저는 없습니다.");
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_MEMBER");
        return new User(member.getName(), passwordEncoder.encode(member.getPassword()),
                Collections.singleton(authority));
    }
}
