package com.coverflow.question.infrastructure;

import com.coverflow.question.domain.Question;
import com.coverflow.question.dto.request.FindQuestionAdminRequest;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.coverflow.question.domain.QQuestion.question;

@Repository
@RequiredArgsConstructor
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

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
    public Optional<Page<Question>> findWithFilters(
            final Pageable pageable,
            final FindQuestionAdminRequest request
    ) {
        List<Question> questions;
        long total;

        CompletableFuture<List<Question>> questionsFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .selectFrom(question)
                        .where(
                                toCreatedDateBetween(request.createdStartDate(), request.createdEndDate()),
                                eqStatus(request.status())
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(makeOrderSpecifiers(question, pageable))
                        .fetch()
        );

        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .select(question.count())
                        .from(question)
                        .where(
                                toCreatedDateBetween(request.createdStartDate(), request.createdEndDate()),
                                eqStatus(request.status())
                        )
                        .fetchOne()
        );

        CompletableFuture.allOf(questionsFuture, countFuture).join();

        try {
            questions = questionsFuture.get();
            total = countFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(new PageImpl<>(questions, pageable, total));
    }

    private BooleanExpression toContainsCreatedStartDate(final String startDate) {
        if (!StringUtils.hasText(startDate)) {
            return null;
        }
        return question.createdAt.goe(LocalDate.parse(startDate).atStartOfDay());
    }

    private BooleanExpression toContainsCreatedEndDate(final String endDate) {
        if (!StringUtils.hasText(endDate)) {
            return null;
        }
        return question.createdAt.loe(LocalDate.parse(endDate).atStartOfDay());
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

    private BooleanExpression eqStatus(final Boolean status) {
        if (status == null) {
            return null;
        }
        return question.questionStatus.eq(status);
    }
}
