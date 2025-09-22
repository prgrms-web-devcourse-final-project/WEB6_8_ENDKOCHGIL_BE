package com.back.domain.party.party.service;

import com.back.domain.member.entity.Member;
import com.back.domain.member.repository.MemberRepository;
import com.back.domain.party.party.dto.PartyRequestDto;
import com.back.domain.party.party.entity.Party;
import com.back.domain.party.party.entity.PartyMember;
import com.back.domain.party.party.entity.PartyMemberStatus;
import com.back.domain.party.party.repository.PartyMemberRepository;
import com.back.domain.party.party.repository.PartyRepository;
import com.back.global.exception.CustomException;
import com.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final PartyMemberRepository partyMemberRepository;

    @Transactional
    public Party createParty(PartyRequestDto requestDto, Integer memberId) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Party newParty = new Party();
        newParty.setName(requestDto.getName());
        newParty.setMaxMembers(requestDto.getMaxMembers());
        newParty.setPublic(requestDto.getIsPublicStatus());
        newParty.setLeader(leader);
        Party savedParty = partyRepository.save(newParty);

        PartyMember partyMember = new PartyMember();
        partyMember.setParty(savedParty);
        partyMember.setMember(leader);
        partyMember.setStatus(PartyMemberStatus.ACCEPTED);
        partyMember.setJoinedAt(LocalDateTime.now());
        partyMemberRepository.save(partyMember);

        return savedParty;
    }

    @Transactional
    public void joinParty(Integer partyId, Integer memberId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!party.isPublic()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "비공개 파티는 직접 가입할 수 없습니다.");
        }

        if (party.getPartyMembers().size() >= party.getMaxMembers()) {
            throw new CustomException(ErrorCode.CONFLICT, "파티의 정원이 가득 찼습니다.");
        }

        if (partyMemberRepository.findByParty_IdAndMember_Id(partyId, memberId).isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT, "이미 가입된 파티입니다.");
        }

        PartyMember partyMember = new PartyMember();
        partyMember.setParty(party);
        partyMember.setMember(member);
        partyMember.setStatus(PartyMemberStatus.ACCEPTED);
        partyMember.setJoinedAt(LocalDateTime.now());
        partyMemberRepository.save(partyMember);
    }

    @Transactional
    public void leaveParty(Integer partyId, Integer memberId) {
        PartyMember leavingMember = partyMemberRepository.findByParty_IdAndMember_Id(partyId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Party party = leavingMember.getParty();

        // 파티장인지 확인
        if (party.getLeader().getId() == memberId) {
            // 본인을 제외한 모든 멤버를 가입일 순으로 정렬하여 조회
            List<PartyMember> otherMembers = partyMemberRepository.findByParty_Id(partyId);
            otherMembers.removeIf(pm -> pm.getMember().getId() == memberId);
            otherMembers.sort(Comparator.comparing(PartyMember::getJoinedAt));

            // 남은 멤버가 있다면 새로운 파티장 위임
            if (!otherMembers.isEmpty()) {
                PartyMember newLeaderMember = otherMembers.getFirst();
                party.setLeader(newLeaderMember.getMember());
            } else {
                // 남은 멤버가 없다면 파티 삭제
                partyRepository.delete(party);
            }
        }

        // 파티 멤버 목록에서 탈퇴 멤버 삭제
        partyMemberRepository.delete(leavingMember);
    }
}