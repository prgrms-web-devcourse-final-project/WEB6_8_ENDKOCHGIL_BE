package com.back.domain.member.entity;

import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SoftDelete
public class Member extends BaseEntity {
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private int level;
    private int xp;
    private int money;
    private MemberRole role;

    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.level = 1;
        this.xp = 0;
        this.money = 0;
        this.role = MemberRole.USER;
    }

    public void modifyPassword(String password) {
        this.password = password;
    }

    public void modifyName(String name) {
        this.name = name;
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
}