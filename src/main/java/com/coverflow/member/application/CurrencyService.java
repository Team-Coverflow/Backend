package com.coverflow.member.application;

import com.coverflow.member.domain.Member;
import com.coverflow.member.exception.MemberException;
import com.coverflow.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final MemberRepository memberRepository;

    /**
     * [출석 체크 메서드]
     * 당일 첫 로그인 시 화폐 5 증가
     */
    @Transactional
    public void dailyCheck(final UUID username) {
        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        // 오늘 첫 로그인 시 = 출석
        if (null == member.getConnectedAt() || !LocalDate.now().equals(member.getConnectedAt().toLocalDate())) {
            // 출석 체크 시 붕어빵 지급
            member.updateFishShapedBun(member.getFishShapedBun() + 5);
        }
    }

    /**
     * [질문 작성 시 화폐 감소 메서드]
     * 질문 작성 시 화폐 10 감소
     */
    @Transactional
    public void writeQuestion(
            final String memberId,
            final int currency
    ) {
        Member member = memberRepository.findById(UUID.fromString(memberId))
                .orElseThrow(() -> new MemberException.MemberNotFoundException(memberId));

        if (member.getFishShapedBun() >= 10 + currency) {
            member.updateFishShapedBun(member.getFishShapedBun() - 10 - currency);
            return;
        }
        throw new MemberException.NotEnoughCurrencyException();
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
        Member member = memberRepository.findById(UUID.fromString(username))
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
        Member member = memberRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));

        member.updateFishShapedBun(member.getFishShapedBun() - currency);
    }
}
