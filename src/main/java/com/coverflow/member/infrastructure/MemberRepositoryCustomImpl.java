package com.coverflow.member.infrastructure;

import com.coverflow.member.domain.Member;
import com.coverflow.member.domain.MemberStatus;
import com.coverflow.member.dto.request.FindMemberAdminRequest;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.coverflow.member.domain.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

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
    public Optional<Page<Member>> findWithFilters(
            final Pageable pageable,
            final FindMemberAdminRequest request
    ) {
        List<Member> members;
        long total;

        CompletableFuture<List<Member>> membersFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .selectFrom(member)
                        .where(
                                toCreatedDateBetween(request.createdStartDate(), request.createdEndDate()),
                                eqMemberStatus(request.status())
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(makeOrderSpecifiers(member, pageable))
                        .fetch()
        );

        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .select(member.count())
                        .from(member)
                        .where(
                                toCreatedDateBetween(request.createdStartDate(), request.createdEndDate()),
                                eqMemberStatus(request.status())
                        )
                        .fetchOne()
        );

        CompletableFuture.allOf(membersFuture, countFuture).join();

        try {
            members = membersFuture.get();
            total = countFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(new PageImpl<>(members, pageable, total));
    }

    private BooleanExpression toContainsCreatedStartDate(String startDate) {
        if (startDate == null) {
            return null;
        }
        return member.createdAt.goe(LocalDate.parse(startDate).atStartOfDay()); // 시작 날짜 이후
    }

    private BooleanExpression toContainsCreatedEndDate(String endDate) {
        if (endDate == null) {
            return null;
        }
        return member.createdAt.loe(LocalDate.parse(endDate).atStartOfDay()); // 종료 날짜 이전
    }

    private BooleanExpression toCreatedDateBetween(String startDate, String endDate) {
        return toContainsCreatedStartDate(startDate).and(toContainsCreatedEndDate(endDate));
    }

    private BooleanExpression eqMemberStatus(final String memberStatus) {
        if (!StringUtils.hasText(memberStatus)) {
            return null;
        }
        return member.memberStatus.eq(MemberStatus.valueOf(memberStatus));
    }
}
