package wanderhub.server.domain.accompany_member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccompanyMember is a Querydsl query type for AccompanyMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccompanyMember extends EntityPathBase<AccompanyMember> {

    private static final long serialVersionUID = -1530119243L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccompanyMember accompanyMember = new QAccompanyMember("accompanyMember");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    public final wanderhub.server.domain.accompany.entity.QAccompany accompany;

    public final NumberPath<Long> accompanyMemberId = createNumber("accompanyMemberId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final wanderhub.server.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QAccompanyMember(String variable) {
        this(AccompanyMember.class, forVariable(variable), INITS);
    }

    public QAccompanyMember(Path<? extends AccompanyMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccompanyMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccompanyMember(PathMetadata metadata, PathInits inits) {
        this(AccompanyMember.class, metadata, inits);
    }

    public QAccompanyMember(Class<? extends AccompanyMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accompany = inits.isInitialized("accompany") ? new wanderhub.server.domain.accompany.entity.QAccompany(forProperty("accompany")) : null;
        this.member = inits.isInitialized("member") ? new wanderhub.server.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

