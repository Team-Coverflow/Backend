package com.coverflow.enquiry.infrastructure;

import com.coverflow.enquiry.domain.Enquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    @Query("SELECT e " +
            "FROM Enquiry e " +
            "WHERE e.member.id = :memberId " +
            "AND e.status = '등록'")
    Optional<Page<Enquiry>> findAllByMemberIdAndStatus(
            @Param("memberId") final UUID memberId,
            final Pageable pageable
    );

    @Query("SELECT e " +
            "FROM Enquiry e ")
    Optional<Page<Enquiry>> findEnquiries(final Pageable pageable);

    @Query("SELECT e " +
            "FROM Enquiry e " +
            "WHERE e.status = :status")
    Optional<Page<Enquiry>> findAllByStatus(
            @Param("status") final String status,
            final Pageable pageable
    );
}
