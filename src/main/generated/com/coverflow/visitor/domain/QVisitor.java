package com.coverflow.visitor.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVisitor is a Querydsl query type for Visitor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVisitor extends EntityPathBase<Visitor> {

    private static final long serialVersionUID = -839858150L;

    public static final QVisitor visitor = new QVisitor("visitor");

    public final com.coverflow.global.entity.QBaseTimeEntity _super = new com.coverflow.global.entity.QBaseTimeEntity(this);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath today = createString("today");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QVisitor(String variable) {
        super(Visitor.class, forVariable(variable));
    }

    public QVisitor(Path<? extends Visitor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVisitor(PathMetadata metadata) {
        super(Visitor.class, metadata);
    }

}

