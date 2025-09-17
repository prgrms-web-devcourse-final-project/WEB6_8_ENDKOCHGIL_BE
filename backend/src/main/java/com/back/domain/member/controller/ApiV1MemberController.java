package com.back.domain.member.controller;

import com.back.domain.member.dto.MemberDto;
import com.back.domain.member.dto.MemberSignupReqDto;
import com.back.domain.member.entity.Member;
import com.back.domain.member.service.MemberService;
import com.back.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "ApiV1MemberController", description = "API 회원 컨트롤러")
public class ApiV1MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원 가입 API")
    public ResponseEntity<ApiResponse<MemberDto>> signup(
            @Valid @RequestBody MemberSignupReqDto reqBody
    ) {
        Member member = memberService.signup(
                reqBody.email(),
                reqBody.password(),
                reqBody.name(),
                reqBody.age(),
                reqBody.gender()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        "201",
                        "회원가입 성공",
                        new MemberDto(member))
                );
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원 로그인 API")
    public void login(

    ) {

    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "회원 로그아웃 API")
    public void logout(

    ) {

    }
}
