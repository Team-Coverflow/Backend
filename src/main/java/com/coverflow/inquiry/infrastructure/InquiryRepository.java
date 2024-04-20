package com.coverflow.inquiry.infrastructure;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.domain.InquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquiryCustomRepository {

    @Query("""
            SELECT count(*)
            FROM Inquiry e
            WHERE e.member.id = :memberId
            AND e.inquiryStatus = :inquiryStatus
            """)
    int findAllCountByMemberId(
            @Param("memberId") final UUID memberId,
            @Param("inquiryStatus") final InquiryStatus inquiryStatus
    );

    @Query("""
            SELECT e
            FROM Inquiry e
            WHERE e.member.id = :memberId
            AND e.inquiryStatus != 'DELETION'
            """)
    Optional<Page<Inquiry>> findAllByMemberIdAndStatus(
            final Pageable pageable,
            @Param("memberId") final UUID memberId
    );

    void deleteByMemberId(UUID id);
}
