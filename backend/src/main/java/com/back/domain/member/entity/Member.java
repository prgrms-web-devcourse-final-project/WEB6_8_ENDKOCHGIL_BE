package com.back.domain.member.entity;

import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SoftDelete
public class Member extends BaseEntity {
    //회원 정보
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private int age;
    private MemberGender gender;

    //상태 및 아이템 정보
    private int level = 1;
    private int xp = 0;
    private int money = 0;

    //개발자용 정보
    private MemberRole role = MemberRole.USER;
    private String apiKey;

    //생성자(회원 가입)
    public Member(String email, String password, String name, int age, MemberGender gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;

        this.apiKey = UUID.randomUUID().toString();
    }

    //생성자(SecurityUser용)
    public Member(int id, String email) {
        setId(id);
        this.email = email;
    }

    // *** Modify 메소드 ***
    public void modifyPassword(String password) {
        this.password = password;
    }

    public void modifyName(String name) {
        this.name = name;
    }

    public void modifyAge(int age) {
        this.age = age;
    }

    public void modifyGender(MemberGender gender) {
        this.gender = gender;
    }

    public void modifyLevel(int level) {
        this.level = level;
    }

    public void modifyXp(int xp) {
        this.xp = xp;
    }

    public void modifyMoney(int money) {
        this.money = money;
    }

    //
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        if (this.role == MemberRole.ADMIN) {
            authorities.add("ROLE_ADMIN");
        }

        return authorities;
    }
}