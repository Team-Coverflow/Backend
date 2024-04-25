package com.coverflow.notification.infrastructure;

import com.coverflow.notification.domain.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.coverflow.notification.domain.QNotification.notification;

@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<Notification>> findByMemberId(final UUID memberId, final LocalDateTime date) {
        List<Notification> notifications = jpaQueryFactory
                .select(notification)
                .where()
                .orderBy(notification.createdAt.desc())
                .limit(10)
                .fetch();

        return Optional.empty();
    }
}
