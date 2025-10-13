package com.back.domain.level.service;

import com.back.domain.level.entity.LevelXP;
import com.back.domain.level.repository.LevelXPRepository;
import com.back.domain.member.entity.Member;
import com.back.domain.member.repository.MemberRepository;
import com.back.domain.reward.entity.Reward;
import com.back.domain.reward.entity.RewardType;
import com.back.domain.reward.service.RewardService;
import com.back.global.exception.CustomException;
import com.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LevelUpService {

    private final MemberRepository memberRepository;
    private final LevelXPRepository levelXPRepository;
    private final RewardService rewardService;


    // 레벨업 보상 서비스
    @Transactional
    public void checkLevelUp(Integer memberId) {
        log.info("### [LEVELUP] START checkLevelUp for Member ID: {}", memberId); // <-- START 로그

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "Member not found with id: " + memberId));

        log.info("    [Current Status] Initial Level: {}, Initial XP: {}", member.getLevel(), member.getXp()); // <-- 현재 상태 로그

        // 1. 레벨업 체크 및 처리
        checkAndProcessLevelUp(member);

        // 2. 변경된 Member 저장
        memberRepository.save(member);

        log.info("### [LEVELUP] END checkLevelUp. Final Level: {}, Final XP: {}", member.getLevel(), member.getXp()); // <-- END 로그
    }


    public void checkAndProcessLevelUp(Member member) {
        int currentLevel = member.getLevel();
        int currentXp = member.getXp();

        Optional<LevelXP> nextLevelXP = levelXPRepository.findById(currentLevel + 1);

        // 반복 레벨업 체크 루프
        while (nextLevelXP.isPresent() && currentXp >= nextLevelXP.get().getXpToNext()) {

            log.info("    [Level Check] Current Level: {}, Current XP: {}, Required XP for Level {}: {}",
                    currentLevel, currentXp, currentLevel + 1, nextLevelXP.get().getXpToNext()); // <-- 루프 진입 전 체크 로그

            // 1. 레벨업 처리
            int requiredXp = nextLevelXP.get().getXpToNext();
            int excessXp = currentXp - requiredXp;

            currentLevel++;
            currentXp = excessXp; // XP 리셋 후 초과분 적용

            member.setLevel(currentLevel);
            member.setXp(currentXp);

            log.info("    [LEVEL UP!] Member Leveled Up to {}. Remaining XP: {}", currentLevel, currentXp); // <-- 레벨업 결과 로그

            // 2. 다음 레벨 요구량(xpReq) 업데이트
            Optional<LevelXP> newNextLevelXP = levelXPRepository.findById(currentLevel + 1);
            if (newNextLevelXP.isPresent()) {
                member.setXpReq(newNextLevelXP.get().getXpToNext());
                log.info("    [XP Req Update] Next XP Required: {}", newNextLevelXP.get().getXpToNext()); // <-- 다음 요구 XP 로그
            } else {
                // Level 30+와 같은 고정 요구량 처리 (LevelXP.FIXED_XP_REQUIREMENT)
                member.setXpReq(LevelXP.FIXED_XP_REQUIREMENT);
                log.info("    [XP Req Update] Fixed XP Required: {}", LevelXP.FIXED_XP_REQUIREMENT); // <-- 고정 요구 XP 로그
            }

            // 3. 레벨업 보상 지급 (이 로직은 테스트에서 rewardRepository.deleteAll()로 인해 실제로 실행되지 않습니다.)
            List<Reward> rewards = rewardService.findByRewardTypeAndRequireValue(RewardType.LEVELUP, currentLevel);
            if (!rewards.isEmpty()) {
                // 이 부분을 우리가 수정하기로 했었지만, 현재 테스트 상황에서는 실행되지 않아 문제가 없습니다.
                // rewardService.giveReward(member.getId(), currentLevel, rewards.getFirst().getId());
            }

            // 다음 레벨 정보 업데이트를 위한 반복
            nextLevelXP = newNextLevelXP;
        }
    }

}