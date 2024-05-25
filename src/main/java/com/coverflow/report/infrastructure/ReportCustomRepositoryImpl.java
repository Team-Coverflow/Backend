package com.coverflow.report.infrastructure;

import com.coverflow.report.domain.Report;
import com.coverflow.report.domain.ReportType;
import com.coverflow.report.dto.request.FindReportAdminRequest;
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
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.coverflow.report.domain.QReport.report;

@Repository
@RequiredArgsConstructor
public class ReportCustomRepositoryImpl implements ReportCustomRepository {

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
    public Optional<Page<Report>> findWithFilters(
            final Pageable pageable,
            final FindReportAdminRequest request
    ) {
        List<Report> reports = jpaQueryFactory
                .selectFrom(report)
                .where(
                        toCreatedDateBetween(request.createdStartDate(), request.createdEndDate()),
                        eqContent(request.content()),
                        eqStatus(request.status()),
                        eqType(request.type())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(makeOrderSpecifiers(report, pageable))
                .fetch();

        return Optional.of(new PageImpl<>(reports, pageable, reports.size()));
    }

    private BooleanExpression toContainsCreatedStartDate(final String startDate) {
        if (!StringUtils.hasText(startDate)) {
            return null;
        }
        return report.createdAt.goe(LocalDate.parse(startDate).atStartOfDay());
    }

    private BooleanExpression toContainsCreatedEndDate(final String endDate) {
        if (!StringUtils.hasText(endDate)) {
            return null;
        }
        return report.createdAt.loe(LocalDate.parse(endDate).atStartOfDay());
    }

    private BooleanExpression toCreatedDateBetween(final String startDate, final String endDate) {
        try {
            BooleanExpression booleanExpression = Objects.requireNonNull(toContainsCreatedStartDate(startDate)).and(toContainsCreatedEndDate(endDate));
            if (booleanExpression == null) {
                throw new NullPointerException();
            }
            return booleanExpression;
        } catch (NullPointerException npe) {
            return null;
        }
    }

    private BooleanExpression eqContent(final String content) {
        if (!StringUtils.hasText(content)) {
            return null;
        }
        return report.content.eq(content);
    }

    private BooleanExpression eqStatus(final Boolean status) {
        if (status == null) {
            return null;
        }
        return report.reportStatus.eq(status);
    }

    private BooleanExpression eqType(final String type) {
        if (!StringUtils.hasText(type)) {
            return null;
        }
        return report.type.eq(ReportType.valueOf(type));
    }
}
