package com.back.domain.mission.entitiy;

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
@Table(name= "sub_goals")
public class SubGoal extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    private String title;
    private Integer orderNum;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "subGoal",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories;
}
