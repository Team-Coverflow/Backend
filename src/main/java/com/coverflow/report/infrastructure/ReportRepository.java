package com.coverflow.report.infrastructure;

import com.coverflow.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, ReportCustomRepository {

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

    void deleteByMemberId(UUID id);
}
