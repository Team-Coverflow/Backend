package com.coverflow.report.infrastructure;

import com.coverflow.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r " +
            "FROM Report r " +
            "WHERE r.member.id = :memberId " +
            "AND r.status = '등록'")
    Optional<List<Report>> findReportsByMemberId(@Param("memberId") final UUID memberId);

    @Query("SELECT r " +
            "FROM Report r ")
    Optional<Page<Report>> findAllReports(final Pageable pageable);
}
