package com.back.domain.member.dto;

import com.back.domain.member.entity.MemberGender;
import jakarta.validation.constraints.NotBlank;

public record MemberSignupReqDto(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        @NotBlank
        int age,
        @NotBlank
        MemberGender gender
) {

}