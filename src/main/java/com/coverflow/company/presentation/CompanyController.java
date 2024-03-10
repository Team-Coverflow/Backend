package com.coverflow.company.presentation;

import com.coverflow.company.application.CompanyService;
import com.coverflow.company.domain.CompanyStatus;
import com.coverflow.company.dto.request.SaveCompanyRequest;
import com.coverflow.company.dto.request.UpdateCompanyRequest;
import com.coverflow.company.dto.response.FindAllCompaniesResponse;
import com.coverflow.company.dto.response.FindCompanyResponse;
import com.coverflow.company.dto.response.SearchCompanyResponse;
import com.coverflow.global.annotation.AdminAuthorize;
import com.coverflow.global.handler.ResponseHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    @GetMapping
    public ResponseEntity<ResponseHandler<List<SearchCompanyResponse>>> searchCompanies(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "name") @NotBlank final String name
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<SearchCompanyResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(companyService.searchCompanies(pageNo, name))
                        .build()
                );
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<ResponseHandler<FindCompanyResponse>> findCompanyById(
            @PathVariable @Positive final long companyId,
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<FindCompanyResponse>builder()
                        .statusCode(HttpStatus.OK)
                        .data(companyService.findCompanyById(pageNo, criterion, companyId))
                        .build()
                );
    }

    @GetMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<List<FindAllCompaniesResponse>>> findAllCompanies(
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion
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
            @RequestParam @PositiveOrZero final int pageNo,
            @RequestParam(defaultValue = "createdAt") @NotBlank final String criterion,
            @RequestParam @NotBlank final CompanyStatus companyStatus
    ) {
        return ResponseEntity.ok()
                .body(ResponseHandler.<List<FindAllCompaniesResponse>>builder()
                        .statusCode(HttpStatus.OK)
                        .data(companyService.findPending(pageNo, criterion, companyStatus))
                        .build()
                );
    }

    @PostMapping
    public ResponseEntity<ResponseHandler<Void>> saveCompany(
            @RequestBody @Valid final SaveCompanyRequest saveCompanyRequest
    ) {
        companyService.saveCompany(saveCompanyRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.CREATED)
                        .build());
    }

    @PatchMapping("/admin/{companyId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateCompanyStatus(
            @PathVariable @Positive final long companyId
    ) {
        companyService.updateCompanyStatus(companyId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @PutMapping("/admin")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> updateCompany(
            @RequestBody @Valid final UpdateCompanyRequest updateCompanyRequest
    ) {
        companyService.updateCompany(updateCompanyRequest);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/admin/{companyId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteCompany(
            @PathVariable @Positive final long companyId
    ) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }

    @DeleteMapping("/admin/real/{companyId}")
    @AdminAuthorize
    public ResponseEntity<ResponseHandler<Void>> deleteCompanyReal(
            @PathVariable @Positive final long companyId
    ) {
        companyService.deleteCompanyReal(companyId);
        return ResponseEntity.ok()
                .body(ResponseHandler.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT)
                        .build());
    }
}
