package wanderhub.server.domain.mytrip_plan_detail.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyTripPlanDetail is a Querydsl query type for MyTripPlanDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyTripPlanDetail extends EntityPathBase<MyTripPlanDetail> {

    private static final long serialVersionUID = 1110625222L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyTripPlanDetail myTripPlanDetail = new QMyTripPlanDetail("myTripPlanDetail");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    public final StringPath content = createString("content");

    public final NumberPath<Double> coordinateX = createNumber("coordinateX", Double.class);

    public final NumberPath<Double> coordinateY = createNumber("coordinateY", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final wanderhub.server.domain.mytrip_plan.entity.QMyTripPlan myTripPlan;

    public final NumberPath<Long> myTripPlanDetailId = createNumber("myTripPlanDetailId", Long.class);

    public final StringPath placeName = createString("placeName");

    public final StringPath subTitle = createString("subTitle");

    public final TimePath<java.time.LocalTime> timeEnd = createTime("timeEnd", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> timeStart = createTime("timeStart", java.time.LocalTime.class);

    public final DatePath<java.time.LocalDate> whenDate = createDate("whenDate", java.time.LocalDate.class);

    public QMyTripPlanDetail(String variable) {
        this(MyTripPlanDetail.class, forVariable(variable), INITS);
    }

    public QMyTripPlanDetail(Path<? extends MyTripPlanDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyTripPlanDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyTripPlanDetail(PathMetadata metadata, PathInits inits) {
        this(MyTripPlanDetail.class, metadata, inits);
    }

    public QMyTripPlanDetail(Class<? extends MyTripPlanDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.myTripPlan = inits.isInitialized("myTripPlan") ? new wanderhub.server.domain.mytrip_plan.entity.QMyTripPlan(forProperty("myTripPlan"), inits.get("myTripPlan")) : null;
    }

}

