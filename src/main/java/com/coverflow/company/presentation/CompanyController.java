package com.coverflow.company.presentation;

import com.coverflow.company.application.CompanyService;
import com.coverflow.company.dto.request.CompanyRequest;
import com.coverflow.company.dto.response.CompanyResponse;
import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/company")
@RequiredArgsConstructor
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/auto-complete")
    public ResponseEntity<ResponseHandler<List<CompanyResponse>>> autoComplete(
            final @RequestParam("name") @Valid String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<CompanyResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("자동 완성 검색에 성공했습니다.")
                        .data(companyService.autoComplete(name))
                        .build()
                );
    }

    @GetMapping("/search-companies")
    public ResponseEntity<ResponseHandler<List<CompanyResponse>>> searchCompanies(
            final @RequestParam("name") @Valid String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<CompanyResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 검색에 성공했습니다.")
                        .data(companyService.searchCompanies(name))
                        .build()
                );
    }

    @GetMapping("/find-company")
    public ResponseEntity<ResponseHandler<CompanyResponse>> findCompanyByName(
            final @RequestParam("name") @Valid String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<CompanyResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 조회에 성공했습니다.")
                        .data(companyService.findCompanyByName(name))
                        .build()
                );
    }

    @GetMapping("/find-all-companies")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<CompanyResponse>>> findAllCompanies() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<CompanyResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("전체 회사 리스트 검색에 성공했습니다.")
                        .data(companyService.findAllCompanies())
                        .build()
                );
    }

    @PostMapping("/save-company")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<CompanyResponse>> saveCompany(
            final @RequestBody @Valid CompanyRequest request
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<CompanyResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 등록에 성공했습니다.")
                        .data(companyService.saveCompany(request))
                        .build());
    }

    @PostMapping("/update-company")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<CompanyResponse>> updateNickname(
            final @RequestBody @Valid CompanyRequest request
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<CompanyResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 수정에 성공했습니다.")
                        .data(companyService.updateCompany(request))
                        .build());
    }

    @PostMapping("/delete-company/{name}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteCompany(
            final @RequestParam("name") @Valid String name
    ) {
        companyService.deleteCompany(name);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 삭제에 성공했습니다.")
                        .build());
    }
}
