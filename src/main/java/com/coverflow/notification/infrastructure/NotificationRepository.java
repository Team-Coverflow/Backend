package com.coverflow.notification.infrastructure;

import com.coverflow.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n " +
            "FROM Notification n " +
            "JOIN FETCH n.member m " +
            "WHERE n.member.id = :member_id " +
            "AND n.status = true " +
            "ORDER BY n.createdAt DESC")
    Optional<List<Notification>> findByMemberId(@Param("member_id") final UUID memberId);
}
