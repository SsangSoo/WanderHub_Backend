package wanderhub.server.domain.accompany.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccompany is a Querydsl query type for Accompany
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccompany extends EntityPathBase<Accompany> {

    private static final long serialVersionUID = -452931416L;

    public static final QAccompany accompany = new QAccompany("accompany");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    public final DatePath<java.time.LocalDate> accompanyEndDate = createDate("accompanyEndDate", java.time.LocalDate.class);

    public final NumberPath<Long> accompanyId = createNumber("accompanyId", Long.class);

    public final StringPath accompanyMaker = createString("accompanyMaker");

    public final ListPath<wanderhub.server.domain.accompany_member.entity.AccompanyMember, wanderhub.server.domain.accompany_member.entity.QAccompanyMember> accompanyMemberList = this.<wanderhub.server.domain.accompany_member.entity.AccompanyMember, wanderhub.server.domain.accompany_member.entity.QAccompanyMember>createList("accompanyMemberList", wanderhub.server.domain.accompany_member.entity.AccompanyMember.class, wanderhub.server.domain.accompany_member.entity.QAccompanyMember.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> accompanyStartDate = createDate("accompanyStartDate", java.time.LocalDate.class);

    public final StringPath content = createString("content");

    public final NumberPath<Double> coordinateX = createNumber("coordinateX", Double.class);

    public final NumberPath<Double> coordinateY = createNumber("coordinateY", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<wanderhub.server.global.utils.Local> local = createEnum("local", wanderhub.server.global.utils.Local.class);

    public final NumberPath<Long> maxMemberCount = createNumber("maxMemberCount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath placeName = createString("placeName");

    public final BooleanPath recruitComplete = createBoolean("recruitComplete");

    public final StringPath title = createString("title");

    public QAccompany(String variable) {
        super(Accompany.class, forVariable(variable));
    }

    public QAccompany(Path<? extends Accompany> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccompany(PathMetadata metadata) {
        super(Accompany.class, metadata);
    }

}

