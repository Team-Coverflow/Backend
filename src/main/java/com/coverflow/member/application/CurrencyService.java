package com.coverflow.member.application;

import com.coverflow.member.domain.Member;
import com.coverflow.member.exception.MemberException;
import com.coverflow.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final MemberRepository memberRepository;

    /**
     * [출석 체크 메서드]
     * 당일 첫 로그인 시 화폐 30 증가
     */
    @Transactional
    public void dailyCheck(final UUID username) {
        final Member member = memberRepository.findById(username)
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        if (!member.getConnected_at().toString().substring(0, 10).equals(LocalDateTime.now().toString().substring(0, 10))) {
            member.updateFishShapedBun(member.getFishShapedBun() + 30);
        }
    }

    /**
     * [질문 작성 시 화폐 감소 메서드]
     * 질문 작성 시 화폐 10 감소
     */
    @Transactional
    public void writeQuestion(final String username) {
        final Member member = memberRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        if (member.getFishShapedBun() >= 10) {
            member.updateFishShapedBun(member.getFishShapedBun() - 10);
            return;
        }
        member.updateFishShapedBun(0);
    }

    /**
     * [화폐 증가 메서드]
     * 원하는 만큼 해당 유저의 화폐 증가
     */
    @Transactional
    public void increaseCurrency(
            final String username,
            final int currency
    ) {
        final Member member = memberRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        member.updateFishShapedBun(member.getFishShapedBun() + currency);
    }

    /**
     * [화폐 감소 메서드]
     * 원하는 만큼 해당 유저의 화폐 감소
     */
    @Transactional
    public void decreaseCurrency(
            final String username,
            final int currency
    ) {
        final Member member = memberRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        member.updateFishShapedBun(member.getFishShapedBun() - currency);
    }
}
