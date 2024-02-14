package com.coverflow.company.presentation;

import com.coverflow.company.application.CompanyService;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.*;
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
    public ResponseEntity<ResponseHandler<List<FindAutoCompleteResponse>>> autoComplete(
            @RequestParam("name") @Valid final String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAutoCompleteResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("자동 완성 검색에 성공했습니다.")
                        .data(companyService.autoComplete(name))
                        .build()
                );
    }

    @GetMapping("/search-companies")
    public ResponseEntity<ResponseHandler<List<SearchCompanyResponse>>> searchCompanies(
            @RequestParam("name") @Valid final String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<SearchCompanyResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 검색에 성공했습니다.")
                        .data(companyService.searchCompanies(name))
                        .build()
                );
    }

    @GetMapping("/find-company/{companyName}")
    public ResponseEntity<ResponseHandler<FindCompanyResponse>> findCompanyByName(
            @PathVariable @Valid final String companyName
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindCompanyResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .message("특정 회사와 질문 조회에 성공했습니다.")
                        .data(companyService.findCompanyByName(companyName))
                        .build()
                );
    }

    @GetMapping("/admin/find-companies")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAllCompaniesResponse>>> findAllCompanies() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAllCompaniesResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("전체 회사 리스트 검색에 성공했습니다.")
                        .data(companyService.findAllCompanies())
                        .build()
                );
    }

    @GetMapping("/admin/find-pending")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindPendingResponse>>> findPending() {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindPendingResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .message("검토 중인 회사 리스트 검색에 성공했습니다.")
                        .data(companyService.findPending())
                        .build()
                );
    }

    @PostMapping("/save-company")
    public ResponseEntity<ResponseHandler<Void>> saveCompany(
            @RequestBody @Valid final SaveCompanyRequest saveCompanyRequest
    ) {
        companyService.saveCompany(saveCompanyRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 등록에 성공했습니다.")
                        .build());
    }

    @PostMapping("/admin/update-company")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateNickname(
            @RequestBody @Valid final UpdateCompanyRequest updateCompanyRequest
    ) {
        companyService.updateCompany(updateCompanyRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 수정에 성공했습니다.")
                        .build());
    }

    @PostMapping("/admin/delete-company/{companyId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteCompany(
            @PathVariable @Valid final Long companyId
    ) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 삭제에 성공했습니다.")
                        .build());
    }

    @PostMapping("/admin/delete-real/{companyId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteCompanyReal(
            @PathVariable @Valid final Long companyId
    ) {
        companyService.deleteCompanyReal(companyId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .message("회사 물리 삭제에 성공했습니다.")
                        .build());
    }
}
