package com.coverflow.company.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -1345599430L;

    public static final QCompany company = new QCompany("company");

    public final com.coverflow.global.entity.QBaseTimeEntity _super = new com.coverflow.global.entity.QBaseTimeEntity(this);

    public final StringPath city = createString("city");

    public final EnumPath<CompanyStatus> companyStatus = createEnum("companyStatus", CompanyStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath district = createString("district");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<com.coverflow.question.domain.Question, com.coverflow.question.domain.QQuestion> questions = this.<com.coverflow.question.domain.Question, com.coverflow.question.domain.QQuestion>createList("questions", com.coverflow.question.domain.Question.class, com.coverflow.question.domain.QQuestion.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata metadata) {
        super(Company.class, metadata);
    }

}

