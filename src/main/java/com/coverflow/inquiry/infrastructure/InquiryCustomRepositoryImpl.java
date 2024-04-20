package com.coverflow.inquiry.infrastructure;

import com.coverflow.inquiry.domain.Inquiry;
import com.coverflow.inquiry.dto.request.FindInquiryAdminRequest;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.coverflow.inquiry.domain.QInquiry.inquiry;

@RequiredArgsConstructor
public class InquiryCustomRepositoryImpl implements InquiryCustomRepository {

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
    public Optional<Page<Inquiry>> findWithFilters(
            final Pageable pageable,
            final FindInquiryAdminRequest request
    ) {
        List<Inquiry> inquiries;
        long total;

        CompletableFuture<List<Inquiry>> inquiriesFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .selectFrom(inquiry)
                        .where(

                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(makeOrderSpecifiers(inquiry, pageable))
                        .fetch()
        );

        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .select(inquiry.count())
                        .from(inquiry)
                        .where(

                        )
                        .fetchOne()
        );

        CompletableFuture.allOf(inquiriesFuture, countFuture).join();

        try {
            inquiries = inquiriesFuture.get();
            total = countFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(new PageImpl<>(inquiries, pageable, total));
    }
}
