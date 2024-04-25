package com.coverflow.notification.infrastructure;

import com.coverflow.notification.domain.Notification;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {
    @Override
    public Optional<List<Notification>> findByMemberId(final UUID memberId, final LocalDateTime date) {
        
        return Optional.empty();
    }
}
