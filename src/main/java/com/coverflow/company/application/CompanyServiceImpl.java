package com.coverflow.company.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.dto.CompaniesDTO;
import com.coverflow.company.dto.CompanyDTO;
import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import com.coverflow.company.dto.request.FindCompanyQuestionRequest;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.FindAllCompaniesResponse;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.dto.response.SearchCompanyCountResponse;
import com.coverflow.company.dto.response.SearchCompanyResponse;
import com.coverflow.company.infrastructure.CompanyRepository;
import com.coverflow.question.application.QuestionServiceImpl;
import com.coverflow.question.dto.CompanyAndQuestionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
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
public class CompanyServiceImpl implements CompanyService {

    private final QuestionServiceImpl questionService;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional(readOnly = true)
    public SearchCompanyResponse search(
            final int pageNo,
            final String name
    ) {
        Slice<Company> companies = companyRepository.findByNameStartingWith(generatePageAsc(pageNo, NORMAL_PAGE_SIZE, "name"), name)
                .orElseThrow(() -> new CompanyNotFoundException(name));

        return SearchCompanyResponse.from(
                companies.getContent()
                        .stream()
                        .map(CompanyDTO::from)
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SearchCompanyCountResponse search(final String name) {
        long totalElements = companyRepository.countByName(name);
        int totalPages = (int) Math.ceil((double) totalElements / NORMAL_PAGE_SIZE);

        return SearchCompanyCountResponse.of(totalPages, totalElements);
    }

    @Override
    @Transactional(readOnly = true)
    public FindCompanyResponse findByCompanyId(
            final int pageNo,
            final String criterion,
            final long companyId,
            final FindCompanyQuestionRequest request
    ) {
        Company company = companyRepository.findRegisteredCompany(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        CompanyAndQuestionDTO questionList = questionService.findByCompanyId(pageNo, criterion, companyId, request.questionTag());

        return FindCompanyResponse.of(company, questionList.getTotalPages(), questionList.getTotalElements(), questionList.getQuestions());
    }

    @Override
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

    @Override
    @Transactional
    public void save(final SaveCompanyRequest request) {
        String modifiedName = "";
        if (request.name().contains("㈜")) {
            modifiedName = request.name().replace("㈜", "(주)");
        }
        if (companyRepository.findByName(request.name()).isPresent()) {
            throw new CompanyExistException(request.name());
        }

        companyRepository.save(new Company(request, modifiedName));
    }

    @Override
    @Transactional
    public void update(
            final long companyId,
            final UpdateCompanyRequest request
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        company.updateCompany(request);
    }

    @Override
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
