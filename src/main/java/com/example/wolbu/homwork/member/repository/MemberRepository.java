package com.example.wolbu.homwork.member.repository;

import com.example.wolbu.homwork.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByName(String name);
}
