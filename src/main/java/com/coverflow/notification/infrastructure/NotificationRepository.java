package com.coverflow.notification.infrastructure;

import com.coverflow.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n " +
            "FROM Notification n " +
            "WHERE n.member.id = :member_id " +
            "AND n.status = '안읽음' " +
            "ORDER BY n.createdAt DESC")
    Optional<List<Notification>> findByMemberId(@Param("member_id") final UUID memberId);

    @Modifying
    @Query("DELETE FROM Notification n " +
            "WHERE n.createdAt< :date")
    void deleteByCreatedAt(@Param("date") final LocalDateTime date);
}
