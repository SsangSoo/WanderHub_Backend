package wanderhub.server.domain.bo_comment_heart.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoCommentHeart is a Querydsl query type for BoCommentHeart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoCommentHeart extends EntityPathBase<BoCommentHeart> {

    private static final long serialVersionUID = 1738436394L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoCommentHeart boCommentHeart = new QBoCommentHeart("boCommentHeart");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    public final wanderhub.server.domain.bo_comment.entity.QBoComment boComment;

    public final NumberPath<Long> boCommentHeartId = createNumber("boCommentHeartId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final wanderhub.server.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QBoCommentHeart(String variable) {
        this(BoCommentHeart.class, forVariable(variable), INITS);
    }

    public QBoCommentHeart(Path<? extends BoCommentHeart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoCommentHeart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoCommentHeart(PathMetadata metadata, PathInits inits) {
        this(BoCommentHeart.class, metadata, inits);
    }

    public QBoCommentHeart(Class<? extends BoCommentHeart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.boComment = inits.isInitialized("boComment") ? new wanderhub.server.domain.bo_comment.entity.QBoComment(forProperty("boComment"), inits.get("boComment")) : null;
        this.member = inits.isInitialized("member") ? new wanderhub.server.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

