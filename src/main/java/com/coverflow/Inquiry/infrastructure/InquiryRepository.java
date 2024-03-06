package com.coverflow.Inquiry.infrastructure;

import com.coverflow.Inquiry.domain.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT e " +
            "FROM Inquiry e " +
            "WHERE e.member.id = :memberId " +
            "AND e.status != '삭제'")
    Optional<Page<Inquiry>> findAllByMemberIdAndStatus(
            final Pageable pageable,
            @Param("memberId") final String memberId
    );

    @Query("SELECT e " +
            "FROM Inquiry e ")
    Optional<Page<Inquiry>> findInquiries(final Pageable pageable);

    @Query("SELECT e " +
            "FROM Inquiry e " +
            "WHERE e.status = :status")
    Optional<Page<Inquiry>> findAllByStatus(
            final Pageable pageable,
            @Param("status") final String status
    );
}
