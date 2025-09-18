package com.back.domain.member.dto;

import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.MemberGender;

public record MemberDto(
        String email,
        String name,
        int age,
        MemberGender gender,
        int level,
        int xp,
        int money
) {
    public MemberDto(Member member) {
        this(
                member.getEmail(),
                member.getName(),
                member.getAge(),
                member.getGender(),
                member.getLevel(),
                member.getXp(),
                member.getMoney()
        );
    }
}