package com.back.domain.mission.entitiy;


import com.back.domain.member.entity.Member;
import com.back.domain.mission.enums.MissionCategory;
import com.back.domain.mission.enums.MissionType;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="missions")
public class Mission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    private String title;

    @Enumerated(EnumType.STRING)
    private MissionCategory category;

    @Enumerated(EnumType.STRING)
    private MissionType type;

    private boolean isCompleted = false;
    private boolean isEditable = true;

    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubGoal> subGoals;


}
