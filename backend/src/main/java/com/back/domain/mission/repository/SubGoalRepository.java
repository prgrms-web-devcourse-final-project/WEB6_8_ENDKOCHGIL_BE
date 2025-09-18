package com.back.domain.mission.repository;

import com.back.domain.mission.entitiy.SubGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubGoalRepository extends JpaRepository<SubGoal, Integer> {
    //특정 미션에 속한 SubGoal whghl
    List<SubGoal> findMissionId(int missionId);
}
