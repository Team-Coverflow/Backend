package com.coverflow.company.application;

import com.coverflow.company.domain.Company;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.exception.CompanyException;
import com.coverflow.company.infrastructure.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * [회사 리스트 검색 메서드]
     * 특정 이름으로 시작하는 회사 리스트를 검색하는 메서드
     */
    public List<FindCompanyResponse> findCompaniesByName(String name) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").descending());
        final List<Company> companies = companyRepository.findByNameStartingWith(name, pageable)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(name));
        final List<FindCompanyResponse> findCompanies = new ArrayList<>();

        for (int i = 0; i < companies.size(); i++) {
            findCompanies.add(i, FindCompanyResponse.of(companies.get(i)));
        }
        return findCompanies;
    }

    /**
     * [회사 조회 메서드]
     * 특정 회사를 조회하는 메서드
     */
    public FindCompanyResponse findCompanyByName(String name) {
        final Company company = companyRepository.findByName(name)
                .orElseThrow(() -> new CompanyException.CompanyNotFoundException(name));
        return FindCompanyResponse.of(company);
    }
}
