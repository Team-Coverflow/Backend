package com.coverflow.company.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.dto.CompaniesDTO;
import com.coverflow.company.dto.CompanyDTO;
import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.FindAllCompaniesResponse;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.dto.response.SearchCompanyResponse;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.question.application.QuestionService;
import com.coverflow.question.dto.CompanyAndQuestionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.coverflow.company.exception.CompanyException.CompanyExistException;
import static com.coverflow.company.exception.CompanyException.CompanyNotFoundException;
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
     * [기업 검색 메서드]
     * 특정 이름으로 시작하는 기업 5개를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public SearchCompanyResponse search(
            final int pageNo,
            final String name
    ) {
        Page<Company> companies = companyRepository.findByNameStartingWithAndCompanyStatus(generatePageAsc(pageNo, NORMAL_PAGE_SIZE, "name"), name)
                .orElseThrow(() -> new CompanyNotFoundException(name));

        return SearchCompanyResponse.of(
                companies.getTotalPages(),
                companies.getTotalElements(),
                companies.getContent()
                        .stream()
                        .map(CompanyDTO::from)
                        .toList()
        );
    }

    /**
     * [특정 기업과 질문 조회 메서드]
     * 특정 기업과 질문 리스트를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public FindCompanyResponse findByCompanyId(
            final int pageNo,
            final String criterion,
            final long companyId
    ) {
        Company company = companyRepository.findRegisteredCompany(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        CompanyAndQuestionDTO questionList = questionService.findByCompanyId(pageNo, criterion, companyId);

        return FindCompanyResponse.of(company, questionList.getTotalPages(), questionList.getQuestions());
    }

    /**
     * [관리자 전용: 기업 조회 메서드]
     * 기업을 필터링해서 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public FindAllCompaniesResponse find(
            final int pageNo,
            final String criterion,
            final FindCompanyAdminRequest request
    ) {
        Page<Company> companies = companyRepository.findWithFilters(generatePageDesc(pageNo, LARGE_PAGE_SIZE, criterion), request)
                .orElseThrow(() -> new CompanyNotFoundException(request));

        return FindAllCompaniesResponse.of(
                companies.getTotalPages(),
                companies.getTotalElements(),
                companies.getContent()
                        .stream()
                        .map(CompaniesDTO::from)
                        .toList()
        );
    }

    /**
     * [기업 등록 메서드]
     */
    @Transactional
    public void save(final SaveCompanyRequest request) {
        if (companyRepository.findByName(request.name()).isPresent()) {
            throw new CompanyExistException(request.name());
        }

        companyRepository.save(new Company(request));
    }

    /**
     * [관리자 전용: 기업 수정 메서드]
     */
    @Transactional
    public void update(
            final long companyId,
            final UpdateCompanyRequest request
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        company.updateCompany(request);
    }

    /**
     * [관리자 전용: 기업 물리 삭제 메서드]
     */
    @Transactional
    public void delete(final long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        companyRepository.delete(company);
    }

    /**
     * 삭제 상태 30일마다 삭제 메서드
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    protected void deletePeriodically() {
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        companyRepository.deleteByCompanyStatus(date);
    }
}
