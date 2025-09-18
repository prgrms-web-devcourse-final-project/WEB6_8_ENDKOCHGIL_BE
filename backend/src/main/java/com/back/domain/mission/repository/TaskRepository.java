package com.back.domain.mission.repository;

import com.back.domain.mission.entitiy.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    //특정 Category 안의 Task 조회
    List<Task> findByCategoryId(int categoryId);

    //특정 날짜의 Task 조회(오늘 할 일)
    List<Task> findByDueDate(LocalDate dueDate);
}
