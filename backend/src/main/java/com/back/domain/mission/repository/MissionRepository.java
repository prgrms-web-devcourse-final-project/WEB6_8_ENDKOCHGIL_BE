package com.back.domain.mission.repository;

import com.back.domain.mission.entitiy.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Integer> {
    List<Mission> findByMemberId(int memberId);
}
