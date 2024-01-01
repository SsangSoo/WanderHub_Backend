package wanderhub.server.domain.mytrip_plan.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyTripPlan is a Querydsl query type for MyTripPlan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyTripPlan extends EntityPathBase<MyTripPlan> {

    private static final long serialVersionUID = -1851817783L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyTripPlan myTripPlan = new QMyTripPlan("myTripPlan");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final wanderhub.server.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail, wanderhub.server.domain.mytrip_plan_detail.entity.QMyTripPlanDetail> myTripPlanDetailList = this.<wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail, wanderhub.server.domain.mytrip_plan_detail.entity.QMyTripPlanDetail>createList("myTripPlanDetailList", wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail.class, wanderhub.server.domain.mytrip_plan_detail.entity.QMyTripPlanDetail.class, PathInits.DIRECT2);

    public final NumberPath<Long> myTripPlanId = createNumber("myTripPlanId", Long.class);

    public final StringPath title = createString("title");

    public final DatePath<java.time.LocalDate> tripEndDate = createDate("tripEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> tripStartDate = createDate("tripStartDate", java.time.LocalDate.class);

    public QMyTripPlan(String variable) {
        this(MyTripPlan.class, forVariable(variable), INITS);
    }

    public QMyTripPlan(Path<? extends MyTripPlan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyTripPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyTripPlan(PathMetadata metadata, PathInits inits) {
        this(MyTripPlan.class, metadata, inits);
    }

    public QMyTripPlan(Class<? extends MyTripPlan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new wanderhub.server.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

