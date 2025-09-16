package com.back.domain.member.service;

import com.back.domain.member.entity.Member;
import com.back.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 가입
    @Transactional
    public Member signup(String email, String password, String name) {
        //검증
        memberRepository
                .findByEmail(email)
                .ifPresent(_member -> {
                    throw new ServiceException("이미 존재하는 아이디입니다.");
                });

        //계정 생성
        password = passwordEncoder.encode(password);
        Member member = new Member(email, password, name);

        return memberRepository.save(member);
    }

    // Find 메소드
    @Transactional(readOnly = true)
    public Optional<Member> findById(int id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    // Modify 메소드
    @Transactional
    public void modify() {

    }
}
