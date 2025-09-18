package com.back.domain.mission.entitiy;

import com.back.domain.mission.enums.TaskStatus;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name= "tasks")
public class Task extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    private String title;
    private LocalDate dueDate; //수행예정일

    private boolean carrayOver = false;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

}
