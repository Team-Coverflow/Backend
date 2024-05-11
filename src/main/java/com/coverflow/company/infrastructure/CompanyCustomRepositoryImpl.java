package com.coverflow.company.infrastructure;

import com.coverflow.company.domain.Company;
import com.coverflow.company.domain.CompanyStatus;
import com.coverflow.company.dto.request.FindCompanyAdminRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

        return Optional.of(new SliceImpl<>(sortedCompanies, pageable, sortedCompanies.size() <= pageable.getPageSize()));
    }

    @Override
    public Long countByName(final String name) {
        return jpaQueryFactory
                .select(company.count())
                .from(company)
                .where(
                        company.name.startsWith(name)
                                .or(company.name.startsWith("(주)" + name)),
                        company.companyStatus.eq(CompanyStatus.valueOf("REGISTRATION"))
                )
                .fetchOne();
    }

    @Override
    public Optional<Slice<Company>> findWithFilters(final Pageable pageable, final FindCompanyAdminRequest request) {
        List<Company> companies = jpaQueryFactory
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
                .fetch();

        return Optional.of(new SliceImpl<>(companies));
    }

    @Override
    public Long countByFilters(final FindCompanyAdminRequest request) {
        return jpaQueryFactory
                .select(company.count())
                .from(company)
                .where(
                        toContainsType(request.type()),
                        toContainsCity(request.city()),
                        toContainsDistrict(request.district()),
                        eqCompanyStatus(request.companyStatus())
                )
                .fetchOne();
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
