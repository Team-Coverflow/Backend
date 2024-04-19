package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;
import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.coverflow.company.domain.QCompany.company;
import static com.coverflow.global.util.RepositoryUtil.makeOrderSpecifiers;

@RequiredArgsConstructor
public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Page<Company>> findWithFilters(
            final Pageable pageable,
            final FindCompanyAdminRequest request
    ) {
        List<Company> companies;
        long total;

        CompletableFuture<List<Company>> companiesFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .selectFrom(company)
                        .where(
                                toContainsType(request.type()),
                                toContainsCity(request.city()),
                                toContainsDistrict(request.district()),
                                eqCompanyStatus(request.companyStatus())
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(makeOrderSpecifiers(company, pageable))
                        .fetch()
        );

        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .select(company.count())
                        .from(company)
                        .where(
                                toContainsType(request.type()),
                                toContainsCity(request.city()),
                                toContainsDistrict(request.district()),
                                eqCompanyStatus(request.companyStatus())
                        )
                        .fetchOne()
        );

        CompletableFuture.allOf(companiesFuture, countFuture).join();

        try {
            companies = companiesFuture.get();
            total = countFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(new PageImpl<>(companies, pageable, total));
    }

    private BooleanExpression toContainsType(final String type) {
        if (!StringUtils.hasText(type)) {
            return null;
        }
        return company.type.contains(type);
    }

    private BooleanExpression toContainsCity(final String city) {
        if (!StringUtils.hasText(city)) {
            return null;
        }
        return company.city.contains(city);
    }

    private BooleanExpression toContainsDistrict(final String district) {
        if (!StringUtils.hasText(district)) {
            return null;
        }
        return company.district.contains(district);
    }

    private BooleanExpression eqCompanyStatus(final String companyStatus) {
        if (!StringUtils.hasText(companyStatus)) {
            return null;
        }
        return company.companyStatus.eq(CompanyStatus.valueOf(companyStatus));
    }
}
