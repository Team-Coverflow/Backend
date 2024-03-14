package com.coverflow.company.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;
import com.coverflow.company.dto.CompanyDTO;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.FindAllCompaniesResponse;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.dto.response.SearchCompanyResponse;
import com.coverflow.company.exception.CompanyException;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.question.application.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.constant.Constant.NORMAL_PAGE_SIZE;
import static com.coverflow.global.util.PageUtil.generatePageAsc;
import static com.coverflow.global.util.PageUtil.generatePageDesc;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final QuestionService questionService;
    private final CompanyRepository companyRepository;

    /**
     * [회사 검색 메서드]
     * 특정 이름으로 시작하는 회사 5개를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public SearchCompanyResponse searchCompanies(
            final int pageNo,
            final String name
    ) {
        Page<Company> companies = companyRepository.findAllByNameStartingWithAndCompanyStatus(generatePageAsc(pageNo, NORMAL_PAGE_SIZE, "name"), name)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(name));

        return new SearchCompanyResponse(
                companies.getTotalPages(),
                companies.getContent()
                        .stream()
                        .map(CompanyDTO::from)
                        .toList()
        );
    }

    /**
     * [특정 회사와 질문 조회 메서드]
     * 특정 회사와 질문 리스트를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public FindCompanyResponse findCompanyById(
            final int pageNo,
            final String criterion,
            final long companyId
    ) {
        Company company = companyRepository.findRegisteredCompany(companyId)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyId));

        return FindCompanyResponse.of(company, questionService.findAllQuestionsByCompanyId(pageNo, criterion, companyId));
    }

    /**
     * [관리자 전용: 전체 회사 조회 메서드]
     * 전체 회사를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<FindAllCompaniesResponse> findAllCompanies(
            final int pageNo,
            final String criterion
    ) {
        Page<Company> companies = companyRepository.findAllCompanies(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion))
                .orElseThrow(CompanyException.CompanyNotFoundException::new);

        return companies.getContent().stream()
                .map(FindAllCompaniesResponse::from)
                .toList();
    }

    /**
     * [관리자 전용: 특정 상태 회사 조회 메서드]
     * 특정 상태(검토/등록/삭제)의 회사를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<FindAllCompaniesResponse> findPending(
            final int pageNo,
            final String criterion,
            final CompanyStatus companyStatus
    ) {
        Page<Company> companies = companyRepository.findAllByCompanyStatus(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), companyStatus)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyStatus));

        return companies.getContent().stream()
                .map(FindAllCompaniesResponse::from)
                .toList();
    }

    /**
     * [회사 등록 메서드]
     */
    @Transactional
    public void saveCompany(final SaveCompanyRequest request) {
        if (companyRepository.findByName(request.name()).isPresent()) {
            throw new CompanyException.CompanyExistException(request.name());
        }

        companyRepository.save(new Company(request));
    }

    /**
     * [관리자 전용: 회사 상태 변경 메서드]
     */
    @Transactional
    public void updateCompanyStatus(final long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyId));

        company.updateCompanyStatus(CompanyStatus.REGISTRATION);
    }

    /**
     * [관리자 전용: 회사 수정 메서드]
     */
    @Transactional
    public void updateCompany(final UpdateCompanyRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(request.companyId()));

        company.updateCompany(request);
    }

    /**
     * [관리자 전용: 회사 삭제 메서드]
     */
    @Transactional
    public void deleteCompany(final long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyId));

        company.updateCompanyStatus(CompanyStatus.DELETION);
    }

    /**
     * [관리자 전용: 회사 물리 삭제 메서드]
     */
    @Transactional
    public void deleteCompanyReal(final long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyId));

        companyRepository.delete(company);
    }

    /**
     * 삭제 상태 30일마다 삭제 메서드
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    protected void deleteCompanyPeriodically() {
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        companyRepository.deleteByCompanyStatus(date);
    }
}
