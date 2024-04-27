package com.coverflow.notification.infrastructure;

import com.coverflow.notification.domain.Notification;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.coverflow.notification.domain.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<Notification>> findByMemberId(
            final UUID memberId,
            final LocalDateTime date,
            final Long lastId
    ) {
        List<Notification> notifications = jpaQueryFactory
                .select(notification)
                .from(notification)
                .where(
                        notification.member.id.eq(memberId),
                        notification.createdAt.gt(date),
                        toContainsLastId(lastId)
                )
                .orderBy(notification.createdAt.desc())
                .limit(10)
                .fetch();

        return Optional.of(notifications);
    }

    private BooleanExpression toContainsLastId(final Long lastId) {
        if (lastId == null) {
            return null;
        }
        return notification.id.lt(lastId);
    }
}
