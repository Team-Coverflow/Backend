package com.coverflow.company.presentation;

import com.coverflow.company.application.CompanyService;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.global.response.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/company")
@RequiredArgsConstructor
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/search")
    public ResponseEntity<ResponseHandler<List<FindCompanyResponse>>> findCompany() {
        return ResponseEntity.ok(
                ResponseHandler.<List<FindCompanyResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 검색에 성공했습니다.")
                        .data(companyService.findCompany())
                        .build()
        );
    }
}
