package com.coverflow.company.presentation;

import com.coverflow.company.application.CompanyService;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.global.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/company")
@RequiredArgsConstructor
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/auto-complete")
    public ResponseEntity<ResponseHandler<List<FindCompanyResponse>>> findCompaniesByName(
            final @RequestParam @Valid String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindCompanyResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 리스트 검색에 성공했습니다.")
                        .data(companyService.findCompaniesByName(name))
                        .build()
                );
    }

    @GetMapping("/find-company")
    public ResponseEntity<ResponseHandler<FindCompanyResponse>> findCompanyByName(
            final @RequestParam @Valid String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindCompanyResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 조회에 성공했습니다.")
                        .data(companyService.findCompanyByName(name))
                        .build()
                );
    }
}
