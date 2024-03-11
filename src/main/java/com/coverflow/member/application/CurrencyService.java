package com.coverflow.member.application;

import com.coverflow.member.domain.Member;
import com.coverflow.member.exception.MemberException;
import com.coverflow.member.infrastructure.MemberRepository;
import com.coverflow.notification.application.NotificationService;
import com.coverflow.notification.domain.Notification;
import com.coverflow.notification.domain.NotificationStatus;
import com.coverflow.notification.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    /**
     * [출석 체크 메서드]
     * 당일 첫 로그인 시 화폐 5 증가
     */
    @Transactional
    public void dailyCheck(final UUID username) {
        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new MemberException.MemberNotFoundException(username));
        Notification notification = Notification.builder()
                .type(NotificationType.DAILY)
                .notificationStatus(NotificationStatus.NO)
                .member(member)
                .build();

        // 오늘 첫 로그인 시 = 출석
        if (null == member.getConnectedAt() ||
                !LocalDateTime.now().toString().substring(0, 10).equals(member.getConnectedAt().toString().substring(0, 10))) {
            // 출석 체크 시 붕어빵 지급
            member.updateFishShapedBun(member.getFishShapedBun() + 5);

            // 출석 체크 알림
            notificationService.sendNotification(notification);
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
