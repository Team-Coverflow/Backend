package com.coverflow.notification.infrastructure;

import com.coverflow.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {

//    @Query("""
//            SELECT n
//            FROM Notification n
//            WHERE n.member.id = :member_id
//            AND n.createdAt> :date
//            ORDER BY n.createdAt DESC
//            """)
//    Optional<List<Notification>> findByMemberId(
//            @Param("member_id") final UUID memberId,
//            @Param("date") final LocalDateTime date
//    );

    @Modifying
    @Query("""
            DELETE FROM Notification n
            WHERE n.createdAt< :date
            """)
    void deleteByCreatedAt(@Param("date") final LocalDateTime date);

    void deleteByMemberId(UUID id);
}
