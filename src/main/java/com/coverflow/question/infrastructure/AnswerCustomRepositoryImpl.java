package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Answer;
import com.coverflow.question.domain.AnswerStatus;
import com.coverflow.question.dto.request.FindAnswerAdminRequest;
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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.coverflow.question.domain.QAnswer.answer;

@RequiredArgsConstructor
public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

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
    public Optional<Page<Answer>> findWithFilters(
            final Pageable pageable,
            final FindAnswerAdminRequest request
    ) {
        List<Answer> answers;
        long total;

        CompletableFuture<List<Answer>> answersFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .selectFrom(answer)
                        .where(
                                toCreatedDateBetween(request.createdStartDate(), request.createdEndDate()),
                                eqStatus(request.status())
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(makeOrderSpecifiers(answer, pageable))
                        .fetch()
        );

        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .select(answer.count())
                        .from(answer)
                        .where(
                                toCreatedDateBetween(request.createdStartDate(), request.createdEndDate()),
                                eqStatus(request.status())
                        )
                        .fetchOne()
        );

        CompletableFuture.allOf(answersFuture, countFuture).join();

        try {
            answers = answersFuture.get();
            total = countFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(new PageImpl<>(answers, pageable, total));
    }

    private BooleanExpression toContainsCreatedStartDate(final String startDate) {
        if (!StringUtils.hasText(startDate)) {
            return null;
        }
        return answer.createdAt.goe(LocalDate.parse(startDate).atStartOfDay());
    }

    private BooleanExpression toContainsCreatedEndDate(final String endDate) {
        if (!StringUtils.hasText(endDate)) {
            return null;
        }
        return answer.createdAt.loe(LocalDate.parse(endDate).atStartOfDay());
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

    private BooleanExpression eqStatus(final String status) {
        if (!StringUtils.hasText(status)) {
            return null;
        }
        return answer.answerStatus.eq(AnswerStatus.valueOf(status));
    }
}
