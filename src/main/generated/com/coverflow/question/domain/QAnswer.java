package com.coverflow.question.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswer is a Querydsl query type for Answer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswer extends EntityPathBase<Answer> {

    private static final long serialVersionUID = 508945846L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswer answer = new QAnswer("answer");

    public final com.coverflow.global.entity.QBaseTimeEntity _super = new com.coverflow.global.entity.QBaseTimeEntity(this);

    public final EnumPath<AnswerStatus> answerStatus = createEnum("answerStatus", AnswerStatus.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.coverflow.member.domain.QMember member;

    public final QQuestion question;

    public final ListPath<com.coverflow.report.domain.Report, com.coverflow.report.domain.QReport> reports = this.<com.coverflow.report.domain.Report, com.coverflow.report.domain.QReport>createList("reports", com.coverflow.report.domain.Report.class, com.coverflow.report.domain.QReport.class, PathInits.DIRECT2);

    public final BooleanPath selection = createBoolean("selection");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAnswer(String variable) {
        this(Answer.class, forVariable(variable), INITS);
    }

    public QAnswer(Path<? extends Answer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswer(PathMetadata metadata, PathInits inits) {
        this(Answer.class, metadata, inits);
    }

    public QAnswer(Class<? extends Answer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.coverflow.member.domain.QMember(forProperty("member")) : null;
        this.question = inits.isInitialized("question") ? new QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}

