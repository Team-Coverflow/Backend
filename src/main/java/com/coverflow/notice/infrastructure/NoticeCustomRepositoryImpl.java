package com.coverflow.notice.infrastructure;

import com.coverflow.notice.domain.Notice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.coverflow.global.util.RepositoryUtil.makeOrderSpecifiers;
import static com.coverflow.notice.domain.QNotice.notice;

@Repository
@RequiredArgsConstructor
public class NoticeCustomRepositoryImpl implements NoticeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Page<Notice>> find(final Pageable pageable) {
        List<Notice> notices;
        long total;

        CompletableFuture<List<Notice>> noticesFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .selectFrom(notice)
                        .where(notice.noticeStatus.eq(true))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(makeOrderSpecifiers(notice, pageable))
                        .fetch()
        );

        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() ->
                jpaQueryFactory
                        .select(notice.count())
                        .from(notice)
                        .where(notice.noticeStatus.eq(true))
                        .fetchOne()
        );

        CompletableFuture.allOf(noticesFuture, countFuture).join();

        try {
            notices = noticesFuture.get();
            total = countFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(new PageImpl<>(notices, pageable, total));
    }
}
