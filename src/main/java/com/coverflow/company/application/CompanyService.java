package com.coverflow.company.application;

import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import com.coverflow.company.dto.request.FindCompanyQuestionRequest;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.FindAllCompaniesResponse;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.dto.response.SearchCompanyCountResponse;
import com.coverflow.company.dto.response.SearchCompanyResponse;

public interface CompanyService {

    /**
     * [기업 검색]
     * 특정 이름으로 시작하는 기업 5개를 조회
     */
    SearchCompanyResponse search(final int pageNo, final String name);

    /**
     * [기업 검색 시 데이터 수]
     */
    SearchCompanyCountResponse search(final String name);

    /**
     * [특정 기업과 질문 조회]
     * 특정 기업과 질문 리스트를 조회
     */
    FindCompanyResponse findByCompanyId(
            final int pageNo,
            final String criterion,
            final long companyId,
            final FindCompanyQuestionRequest request
    );

    /**
     * [관리자 전용: 기업 조회 메서드]
     * 기업을 필터링해서 조회하는 메서드
     */
    FindAllCompaniesResponse find(
            final int pageNo,
            final String criterion,
            final FindCompanyAdminRequest request
    );

    /**
     * [기업 등록 메서드]
     */
    void save(final SaveCompanyRequest request);

    /**
     * [관리자 전용: 기업 수정 메서드]
     */
    void update(final long companyId, final UpdateCompanyRequest request);

    /**
     * [관리자 전용: 기업 물리 삭제 메서드]
     */
    void delete(final long companyId);
}
