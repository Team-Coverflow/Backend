package com.coverflow.report.application;

import com.coverflow.report.dto.request.FindReportAdminRequest;
import com.coverflow.report.dto.request.SaveReportRequest;
import com.coverflow.report.dto.request.UpdateReportRequest;
import com.coverflow.report.dto.response.FindReportResponse;

public interface ReportService {

    /**
     * [특정 회원의 신고 리스트 조회 메서드]
     */
    FindReportResponse findMyReport(
            final String memberId,
            final int pageNo,
            final String criterion
    );

    /**
     * [관리자 - 신고 조회 메서드]
     */
    FindReportResponse find(
            final int pageNo,
            final String criterion,
            final FindReportAdminRequest request
    );

    /**
     * [신고 등록 메서드]
     */
    void save(final SaveReportRequest request, final String memberId);

    /**
     * [관리자 - 신고 수정 메서드]
     */
    void update(final long reportId, final UpdateReportRequest request);

    /**
     * [관리자 - 신고 삭제 메서드]
     */
    void delete(final long reportId);
}
