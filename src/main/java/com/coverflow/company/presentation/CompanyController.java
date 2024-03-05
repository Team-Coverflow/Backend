package com.coverflow.company.presentation;

import com.coverflow.company.application.CompanyService;
import com.coverflow.company.domain.CompanyStatus;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.FindAllCompaniesResponse;
import com.coverflow.company.dto.response.FindAutoCompleteResponse;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.dto.response.SearchCompanyResponse;
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
            @RequestParam(defaultValue = "name") @Valid final String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAutoCompleteResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(companyService.autoComplete(name))
                        .build()
                );
    }

    @GetMapping("/companies")
    public ResponseEntity<ResponseHandler<List<SearchCompanyResponse>>> searchCompanies(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "name") @Valid final String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<SearchCompanyResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(companyService.searchCompanies(pageNo, name))
                        .build()
                );
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ResponseHandler<FindCompanyResponse>> findCompanyById(
            @PathVariable @Valid final long companyId,
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindCompanyResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(companyService.findCompanyById(pageNo, criterion, companyId))
                        .build()
                );
    }

    @GetMapping("/admin/companies")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAllCompaniesResponse>>> findAllCompanies(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAllCompaniesResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(companyService.findAllCompanies(pageNo, criterion))
                        .build()
                );
    }

    @GetMapping("/admin/status")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAllCompaniesResponse>>> findPending(
            @RequestParam(defaultValue = "0") @Valid final int pageNo,
            @RequestParam(defaultValue = "createdAt") @Valid final String criterion,
            @RequestParam(defaultValue = "REGISTRATION") @Valid final CompanyStatus companyStatus
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAllCompaniesResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(companyService.findPending(pageNo, criterion, companyStatus))
                        .build()
                );
    }

    @PostMapping("/company")
    public ResponseEntity<ResponseHandler<Void>> saveCompany(
            @RequestBody @Valid final SaveCompanyRequest saveCompanyRequest
    ) {
        companyService.saveCompany(saveCompanyRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }

    @PutMapping("/admin/company")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateCompany(
            @RequestBody @Valid final UpdateCompanyRequest updateCompanyRequest
    ) {
        companyService.updateCompany(updateCompanyRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }

    @DeleteMapping("/admin/company/{companyId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteCompany(
            @PathVariable @Valid final long companyId
    ) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }

    @DeleteMapping("/admin/real/{companyId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteCompanyReal(
            @PathVariable @Valid final long companyId
    ) {
        companyService.deleteCompanyReal(companyId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.OK)
                        .build());
    }
}
