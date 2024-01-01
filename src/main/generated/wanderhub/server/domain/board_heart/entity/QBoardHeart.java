package wanderhub.server.domain.board_heart.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardHeart is a Querydsl query type for BoardHeart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardHeart extends EntityPathBase<BoardHeart> {

    private static final long serialVersionUID = 1074336869L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardHeart boardHeart = new QBoardHeart("boardHeart");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    public final wanderhub.server.domain.board.entity.QBoard board;

    public final NumberPath<Long> boardHeartId = createNumber("boardHeartId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final wanderhub.server.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public QBoardHeart(String variable) {
        this(BoardHeart.class, forVariable(variable), INITS);
    }

    public QBoardHeart(Path<? extends BoardHeart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardHeart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardHeart(PathMetadata metadata, PathInits inits) {
        this(BoardHeart.class, metadata, inits);
    }

    public QBoardHeart(Class<? extends BoardHeart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new wanderhub.server.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new wanderhub.server.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

