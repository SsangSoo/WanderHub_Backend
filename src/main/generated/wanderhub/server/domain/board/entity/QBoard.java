package wanderhub.server.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -2036872312L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    public final ListPath<wanderhub.server.domain.board_heart.entity.BoardHeart, wanderhub.server.domain.board_heart.entity.QBoardHeart> boardHeartList = this.<wanderhub.server.domain.board_heart.entity.BoardHeart, wanderhub.server.domain.board_heart.entity.QBoardHeart>createList("boardHeartList", wanderhub.server.domain.board_heart.entity.BoardHeart.class, wanderhub.server.domain.board_heart.entity.QBoardHeart.class, PathInits.DIRECT2);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final ListPath<wanderhub.server.domain.bo_comment.entity.BoComment, wanderhub.server.domain.bo_comment.entity.QBoComment> boCommentList = this.<wanderhub.server.domain.bo_comment.entity.BoComment, wanderhub.server.domain.bo_comment.entity.QBoComment>createList("boCommentList", wanderhub.server.domain.bo_comment.entity.BoComment.class, wanderhub.server.domain.bo_comment.entity.QBoComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<wanderhub.server.global.utils.Local> local = createEnum("local", wanderhub.server.global.utils.Local.class);

    public final wanderhub.server.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath nickName = createString("nickName");

    public final StringPath title = createString("title");

    public final NumberPath<Long> viewPoint = createNumber("viewPoint", Long.class);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new wanderhub.server.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

