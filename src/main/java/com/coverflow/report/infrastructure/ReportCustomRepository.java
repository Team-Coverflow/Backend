package com.coverflow.report.infrastructure;

import com.coverflow.report.domain.Report;
import com.coverflow.report.dto.request.FindReportAdminRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReportCustomRepository {

    Optional<Page<Report>> findWithFilters(final Pageable pageable, final FindReportAdminRequest request);
}
