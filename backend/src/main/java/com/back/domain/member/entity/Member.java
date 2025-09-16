package com.back.domain.member.entity;

import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private int level;
    private int xp;
    private int money;

    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.level = 1;
        this.xp = 0;
        this.money = 0;
    }
}