package com.coverflow.enquiry.infrastructure;

import com.coverflow.enquiry.domain.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    @Query("SELECT e " +
            "FROM Enquiry e " +
            "WHERE e.member.id = :memberId " +
            "AND e.status = '등록'")
    Optional<List<Enquiry>> findEnquiriesByMemberId(@Param("memberId") UUID memberId);

    @Query("SELECT e " +
            "FROM Enquiry e " +
            "WHERE e.status = '등록'")
    Optional<List<Enquiry>> findEnquiries();
}
