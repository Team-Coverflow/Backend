package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;
import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.coverflow.company.domain.QCompany.company;
import static com.coverflow.global.util.RepositoryUtil.makeOrderSpecifiers;

@Repository
@RequiredArgsConstructor
public class CompanyCustomRepositoryImpl implements CompanyCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Slice<Company>> findByNameStartingWith(final Pageable pageable, final String name) {
        List<Company> companies = jpaQueryFactory
                .selectFrom(company)
                .where(
                        company.name.startsWith(name)
                                .or(company.name.startsWith("(주)" + name)),
                        company.companyStatus.eq(CompanyStatus.valueOf("REGISTRATION"))
                )
                .orderBy(makeOrderSpecifiers(company, pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Comparator<Company> customComparator = Comparator.comparing(c -> {
            String companyName = c.getName();
            if (companyName.startsWith("(주)")) {
                return companyName.substring(3);
            }
            return companyName;
        });

        List<Company> sortedCompanies = companies.stream()
                .sorted(customComparator)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedCompanies.size());
        List<Company> paginatedList = sortedCompanies.subList(start, end);

        return Optional.of(new SliceImpl<>(paginatedList, pageable, sortedCompanies.size() < pageable.getPageSize()));
    }

    @Override
    public Long countByName(final String name) {
        return jpaQueryFactory
                .select(company.count())
                .from(company)
                .where(company.name.startsWith(name))
                .fetchOne();
    }

    @Override
    public Optional<Page<Company>> findWithFilters(final Pageable pageable, final FindCompanyAdminRequest request) {
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
                        .orderBy(makeOrderSpecifiers(company, pageable))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
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
