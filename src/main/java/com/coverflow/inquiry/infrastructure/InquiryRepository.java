package com.coverflow.inquiry.infrastructure;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.domain.InquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("""
            SELECT count(*)
            FROM Inquiry e
            WHERE e.member.id = :memberId
            AND e.inquiryStatus = :inquiryStatus
            """)
    int findAllCountByMemberId(
            @Param("memberId") final String memberId,
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
            @Param("memberId") final String memberId
    );

    @Query("""
            SELECT e
            FROM Inquiry e
            """)
    Optional<Page<Inquiry>> findInquiries(final Pageable pageable);

    @Query("""
            SELECT e
            FROM Inquiry e
            WHERE e.inquiryStatus = :inquiryStatus
            """)
    Optional<Page<Inquiry>> findAllByStatus(
            final Pageable pageable,
            @Param("inquiryStatus") final InquiryStatus inquiryStatus
    );
}
