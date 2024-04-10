package com.coverflow.report.infrastructure;

import com.coverflow.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("""
            SELECT r
            FROM Report r
            WHERE r.member.id = :memberId
            AND r.reportStatus = true
            """)
    Optional<Page<Report>> findByMemberId(
            @Param("memberId") final String memberId,
            final Pageable pageable
    );

    @Query("""
            SELECT r
            FROM Report r
            """)
    Optional<Page<Report>> find(final Pageable pageable);

    @Query("""
            SELECT r
            FROM Report r
            WHERE r.reportStatus = :reportStatus
            """)
    Optional<Page<Report>> findByReportStatus(
            final Pageable pageable,
            @Param("reportStatus") final boolean reportStatus
    );

    void deleteByMemberId(UUID id);
}
