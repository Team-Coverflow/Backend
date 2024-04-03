package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;
import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.coverflow.company.domain.QCompany.company;

@RequiredArgsConstructor
public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public static <T> OrderSpecifier[] makeOrderSpecifiers(final EntityPathBase<T> qClass, final Pageable pageable) {
        return pageable.getSort()
                .stream()
                .map(sort -> toOrderSpecifier(qClass, sort))
                .toList().toArray(OrderSpecifier[]::new);
    }

    private static <T> OrderSpecifier toOrderSpecifier(final EntityPathBase<T> qClass, final Sort.Order sortOrder) {
        final Order orderMethod = toOrder(sortOrder);
        final PathBuilder<T> pathBuilder = new PathBuilder<>(qClass.getType(), qClass.getMetadata());
        return new OrderSpecifier(orderMethod, pathBuilder.get(sortOrder.getProperty()));
    }

    private static Order toOrder(final Sort.Order sortOrder) {
        if (sortOrder.isAscending()) {
            return Order.ASC;
        }
        return Order.DESC;
    }

    @Override
    public Optional<Page<Company>> findWithFilters(
            final Pageable pageable,
            final FindCompanyAdminRequest request
    ) {
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

        CompletableFuture.allOf(companiesFuture, countFuture).join(); // 이 호출로 두 쿼리가 완료될 때까지 대기

        List<Company> companies; // 목록 조회 결과
        long total; // 카운트 조회 결과
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
