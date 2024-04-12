package com.coverflow.report.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReport is a Querydsl query type for Report
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReport extends EntityPathBase<Report> {

    private static final long serialVersionUID = -2112710278L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReport report = new QReport("report");

    public final com.coverflow.global.entity.QBaseTimeEntity _super = new com.coverflow.global.entity.QBaseTimeEntity(this);

    public final com.coverflow.question.domain.QAnswer answer;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.coverflow.member.domain.QMember member;

    public final com.coverflow.question.domain.QQuestion question;

    public final BooleanPath reportStatus = createBoolean("reportStatus");

    public final EnumPath<ReportType> type = createEnum("type", ReportType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReport(String variable) {
        this(Report.class, forVariable(variable), INITS);
    }

    public QReport(Path<? extends Report> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReport(PathMetadata metadata, PathInits inits) {
        this(Report.class, metadata, inits);
    }

    public QReport(Class<? extends Report> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.answer = inits.isInitialized("answer") ? new com.coverflow.question.domain.QAnswer(forProperty("answer"), inits.get("answer")) : null;
        this.member = inits.isInitialized("member") ? new com.coverflow.member.domain.QMember(forProperty("member")) : null;
        this.question = inits.isInitialized("question") ? new com.coverflow.question.domain.QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}

