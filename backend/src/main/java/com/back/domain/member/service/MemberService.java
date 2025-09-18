package com.back.domain.member.service;

import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.MemberGender;
import com.back.domain.member.repository.MemberRepository;
import com.back.global.exception.CustomException;
import com.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
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
    public Member signup(
            String email,
            String password,
            String name,
            int age,
            MemberGender gender
    ) {
        //검증
        memberRepository
                .findByEmail(email)
                .ifPresent(_member -> {
                    throw new CustomException(ErrorCode.CONFLICT, "존재하지 않는 이메일입니다.");
                });

        //계정 생성
        password = passwordEncoder.encode(password);
        Member member = new Member(email, password, name, age, gender);

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
