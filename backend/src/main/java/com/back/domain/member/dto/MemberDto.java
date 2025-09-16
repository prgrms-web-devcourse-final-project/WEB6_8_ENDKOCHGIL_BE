package com.back.domain.member.dto;

import com.back.domain.member.entity.Member;

public record MemberDto(
        String email,
        String name,
        int level,
        int xp,
        int money
) {
    public MemberDto(Member member) {
        this(
                member.getEmail(),
                member.getName(),
                member.getLevel(),
                member.getXp(),
                member.getMoney()
        );
    }
}