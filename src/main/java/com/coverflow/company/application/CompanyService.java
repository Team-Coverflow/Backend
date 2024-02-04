package com.coverflow.company.application;

import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.infrastructure.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<FindCompanyResponse> findCompany() {
        return
    }
}
