package com.coverflow.question.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = -1195621410L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final com.coverflow.global.entity.QBaseTimeEntity _super = new com.coverflow.global.entity.QBaseTimeEntity(this);

    public final NumberPath<Integer> answerCount = createNumber("answerCount", Integer.class);

    public final ListPath<Answer, QAnswer> answers = this.<Answer, QAnswer>createList("answers", Answer.class, QAnswer.class, PathInits.DIRECT2);

    public final com.coverflow.company.domain.QCompany company;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.coverflow.member.domain.QMember member;

    public final StringPath questionCategory = createString("questionCategory");

    public final BooleanPath questionStatus = createBoolean("questionStatus");

    public final EnumPath<QuestionTag> questionTag = createEnum("questionTag", QuestionTag.class);

    public final ListPath<com.coverflow.report.domain.Report, com.coverflow.report.domain.QReport> reports = this.<com.coverflow.report.domain.Report, com.coverflow.report.domain.QReport>createList("reports", com.coverflow.report.domain.Report.class, com.coverflow.report.domain.QReport.class, PathInits.DIRECT2);

    public final NumberPath<Integer> reward = createNumber("reward", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new com.coverflow.company.domain.QCompany(forProperty("company")) : null;
        this.member = inits.isInitialized("member") ? new com.coverflow.member.domain.QMember(forProperty("member")) : null;
    }

}

