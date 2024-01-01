package wanderhub.server.domain.bo_comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoComment is a Querydsl query type for BoComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoComment extends EntityPathBase<BoComment> {

    private static final long serialVersionUID = -1572180637L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoComment boComment = new QBoComment("boComment");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    public final wanderhub.server.domain.board.entity.QBoard board;

    public final ListPath<wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart, wanderhub.server.domain.bo_comment_heart.entity.QBoCommentHeart> boCommentHeartList = this.<wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart, wanderhub.server.domain.bo_comment_heart.entity.QBoCommentHeart>createList("boCommentHeartList", wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart.class, wanderhub.server.domain.bo_comment_heart.entity.QBoCommentHeart.class, PathInits.DIRECT2);

    public final NumberPath<Long> boCommentId = createNumber("boCommentId", Long.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final wanderhub.server.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath nickName = createString("nickName");

    public QBoComment(String variable) {
        this(BoComment.class, forVariable(variable), INITS);
    }

    public QBoComment(Path<? extends BoComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoComment(PathMetadata metadata, PathInits inits) {
        this(BoComment.class, metadata, inits);
    }

    public QBoComment(Class<? extends BoComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new wanderhub.server.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new wanderhub.server.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

