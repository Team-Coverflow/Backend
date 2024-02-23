package com.coverflow.report.infrastructure;

import com.coverflow.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
