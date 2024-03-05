package com.coverflow.company.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.FindAllCompaniesResponse;
import com.coverflow.company.dto.response.FindAutoCompleteResponse;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.dto.response.SearchCompanyResponse;
import com.coverflow.company.exception.CompanyException;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.question.application.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.coverflow.global.constant.Constant.LARGE_PAGE_SIZE;
import static com.coverflow.global.constant.Constant.NORMAL_PAGE_SIZE;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final QuestionService questionService;
    private final CompanyRepository companyRepository;

    /**
     * [자동 완성 메서드]
     * 특정 이름으로 시작하는 회사 5개를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<FindAutoCompleteResponse> autoComplete(final String name) {
        Pageable pageable = PageRequest.of(0, NORMAL_PAGE_SIZE, Sort.by("name").ascending());
        Page<Company> companies = companyRepository.findAllByNameStartingWithAndStatus(pageable, name)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(name));
        List<FindAutoCompleteResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.getContent().size(); i++) {
            findCompanies.add(i, FindAutoCompleteResponse.from(companies.getContent().get(i)));
        }
        return findCompanies;
    }

    /**
     * [회사 검색 메서드]
     * 특정 이름으로 시작하는 회사 5개를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<SearchCompanyResponse> searchCompanies(
            final int pageNo,
            final String name
    ) {
        Pageable pageable = PageRequest.of(pageNo, NORMAL_PAGE_SIZE, Sort.by("name").ascending());
        Page<Company> companies = companyRepository.findAllByNameStartingWithAndStatus(pageable, name)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(name));
        List<SearchCompanyResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.getContent().size(); i++) {
            findCompanies.add(i, SearchCompanyResponse.from(companies.getContent().get(i)));
        }
        return findCompanies;
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
        Pageable pageable = PageRequest.of(pageNo, LARGE_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Company> companies = companyRepository.findAllCompanies(pageable)
                .orElseThrow(CompanyException.CompanyNotFoundException::new);
        List<FindAllCompaniesResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.getContent().size(); i++) {
            findCompanies.add(i, FindAllCompaniesResponse.from(companies.getContent().get(i)));
        }
        return findCompanies;
    }

    /**
     * [관리자 전용: 특정 상태 회사 조회 메서드]
     * 특정 상태(검토/등록/삭제)의 회사를 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<FindAllCompaniesResponse> findPending(
            final int pageNo,
            final String criterion,
            final String status
    ) {
        Pageable pageable = PageRequest.of(pageNo, LARGE_PAGE_SIZE, Sort.by(criterion).descending());
        Page<Company> companies = companyRepository.findAllByStatus(pageable, status)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(status));
        List<FindAllCompaniesResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.getContent().size(); i++) {
            findCompanies.add(i, FindAllCompaniesResponse.from(companies.getContent().get(i)));
        }
        return findCompanies;
    }

    /**
     * [회사 등록 메서드]
     */
    @Transactional
    public void saveCompany(final SaveCompanyRequest request) {
        if (companyRepository.findByName(request.name()).isPresent()) {
            throw new CompanyException.CompanyExistException(request.name());
        }

        Company company = Company.builder()
                .name(request.name())
                .type(request.type())
                .city(request.city())
                .district(request.district())
                .establishment(request.establishment())
                .questionCount(0)
                .status("검토")
                .build();

        companyRepository.save(company);
    }

    /**
     * [관리자 전용: 회사 수정 메서드]
     */
    @Transactional
    public void updateCompany(final UpdateCompanyRequest request) {
        Company company = companyRepository.findByName(request.name())
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(request.name()));

        company.updateCompany(Company.builder()
                .name(request.name())
                .type(request.type())
                .city(request.city())
                .district(request.district())
                .establishment(request.establishment())
                .status(request.status())
                .build());
    }

    /**
     * [관리자 전용: 회사 삭제 메서드]
     */
    @Transactional
    public void deleteCompany(final long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(companyId));

        company.updateStatus("삭제");
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
}
