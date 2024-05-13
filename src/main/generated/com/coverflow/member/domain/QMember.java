package com.coverflow.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1693096390L;

    public static final QMember member = new QMember("member1");

    public final com.coverflow.global.entity.QBaseTimeEntity _super = new com.coverflow.global.entity.QBaseTimeEntity(this);

    public final StringPath age = createString("age");

    public final BooleanPath agreeCollection = createBoolean("agreeCollection");

    public final BooleanPath agreeMarketing = createBoolean("agreeMarketing");

    public final ListPath<com.coverflow.question.domain.Answer, com.coverflow.question.domain.QAnswer> answers = this.<com.coverflow.question.domain.Answer, com.coverflow.question.domain.QAnswer>createList("answers", com.coverflow.question.domain.Answer.class, com.coverflow.question.domain.QAnswer.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> connectedAt = createDateTime("connectedAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> currency = createNumber("currency", Integer.class);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final ListPath<com.coverflow.inquiry.domain.Inquiry, com.coverflow.inquiry.domain.QInquiry> inquiries = this.<com.coverflow.inquiry.domain.Inquiry, com.coverflow.inquiry.domain.QInquiry>createList("inquiries", com.coverflow.inquiry.domain.Inquiry.class, com.coverflow.inquiry.domain.QInquiry.class, PathInits.DIRECT2);

    public final EnumPath<MemberStatus> memberStatus = createEnum("memberStatus", MemberStatus.class);

    public final StringPath nickname = createString("nickname");

    public final ListPath<com.coverflow.notification.domain.Notification, com.coverflow.notification.domain.QNotification> notifications = this.<com.coverflow.notification.domain.Notification, com.coverflow.notification.domain.QNotification>createList("notifications", com.coverflow.notification.domain.Notification.class, com.coverflow.notification.domain.QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final ListPath<com.coverflow.question.domain.Question, com.coverflow.question.domain.QQuestion> questions = this.<com.coverflow.question.domain.Question, com.coverflow.question.domain.QQuestion>createList("questions", com.coverflow.question.domain.Question.class, com.coverflow.question.domain.QQuestion.class, PathInits.DIRECT2);

    public final StringPath refreshToken = createString("refreshToken");

    public final EnumPath<RefreshTokenStatus> refreshTokenStatus = createEnum("refreshTokenStatus", RefreshTokenStatus.class);

    public final ListPath<com.coverflow.report.domain.Report, com.coverflow.report.domain.QReport> reports = this.<com.coverflow.report.domain.Report, com.coverflow.report.domain.QReport>createList("reports", com.coverflow.report.domain.Report.class, com.coverflow.report.domain.QReport.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath socialAccessToken = createString("socialAccessToken");

    public final StringPath socialId = createString("socialId");

    public final EnumPath<SocialType> socialType = createEnum("socialType", SocialType.class);

    public final StringPath tag = createString("tag");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

