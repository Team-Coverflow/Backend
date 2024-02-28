package com.coverflow.report.infrastructure;

import com.coverflow.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r " +
            "FROM Report r " +
            "WHERE r.member.id = :memberId " +
            "AND r.status = '등록'")
    Optional<Page<Report>> findReportsByMemberId(
            @Param("memberId") final String memberId,
            final Pageable pageable
    );

    @Query("SELECT r " +
            "FROM Report r ")
    Optional<Page<Report>> findAllReports(final Pageable pageable);

    @Query("SELECT r " +
            "FROM Report r " +
            "WHERE r.status = :status")
    Optional<Page<Report>> findAllByStatus(
            final Pageable pageable,
            @Param("status") final String status
    );
}
