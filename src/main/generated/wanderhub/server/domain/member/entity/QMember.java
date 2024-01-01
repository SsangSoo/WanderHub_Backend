package wanderhub.server.domain.member.entity;

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

    private static final long serialVersionUID = 340350486L;

    public static final QMember member = new QMember("member1");

    public final wanderhub.server.global.audit.QAuditable _super = new wanderhub.server.global.audit.QAuditable(this);

    public final ListPath<wanderhub.server.domain.accompany_member.entity.AccompanyMember, wanderhub.server.domain.accompany_member.entity.QAccompanyMember> accompanyMemberList = this.<wanderhub.server.domain.accompany_member.entity.AccompanyMember, wanderhub.server.domain.accompany_member.entity.QAccompanyMember>createList("accompanyMemberList", wanderhub.server.domain.accompany_member.entity.AccompanyMember.class, wanderhub.server.domain.accompany_member.entity.QAccompanyMember.class, PathInits.DIRECT2);

    public final ListPath<wanderhub.server.domain.board_heart.entity.BoardHeart, wanderhub.server.domain.board_heart.entity.QBoardHeart> boardHeartList = this.<wanderhub.server.domain.board_heart.entity.BoardHeart, wanderhub.server.domain.board_heart.entity.QBoardHeart>createList("boardHeartList", wanderhub.server.domain.board_heart.entity.BoardHeart.class, wanderhub.server.domain.board_heart.entity.QBoardHeart.class, PathInits.DIRECT2);

    public final ListPath<wanderhub.server.domain.board.entity.Board, wanderhub.server.domain.board.entity.QBoard> boardList = this.<wanderhub.server.domain.board.entity.Board, wanderhub.server.domain.board.entity.QBoard>createList("boardList", wanderhub.server.domain.board.entity.Board.class, wanderhub.server.domain.board.entity.QBoard.class, PathInits.DIRECT2);

    public final ListPath<wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart, wanderhub.server.domain.bo_comment_heart.entity.QBoCommentHeart> boCommentHeartList = this.<wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart, wanderhub.server.domain.bo_comment_heart.entity.QBoCommentHeart>createList("boCommentHeartList", wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart.class, wanderhub.server.domain.bo_comment_heart.entity.QBoCommentHeart.class, PathInits.DIRECT2);

    public final ListPath<wanderhub.server.domain.bo_comment.entity.BoComment, wanderhub.server.domain.bo_comment.entity.QBoComment> boCommentList = this.<wanderhub.server.domain.bo_comment.entity.BoComment, wanderhub.server.domain.bo_comment.entity.QBoComment>createList("boCommentList", wanderhub.server.domain.bo_comment.entity.BoComment.class, wanderhub.server.domain.bo_comment.entity.QBoComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final StringPath imgUrl = createString("imgUrl");

    public final EnumPath<wanderhub.server.global.utils.Local> local = createEnum("local", wanderhub.server.global.utils.Local.class);

    public final EnumPath<MemberStatus> memberStatus = createEnum("memberStatus", MemberStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<wanderhub.server.domain.mytrip_plan.entity.MyTripPlan, wanderhub.server.domain.mytrip_plan.entity.QMyTripPlan> myTripPlanList = this.<wanderhub.server.domain.mytrip_plan.entity.MyTripPlan, wanderhub.server.domain.mytrip_plan.entity.QMyTripPlan>createList("myTripPlanList", wanderhub.server.domain.mytrip_plan.entity.MyTripPlan.class, wanderhub.server.domain.mytrip_plan.entity.QMyTripPlan.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final BooleanPath newbie = createBoolean("newbie");

    public final StringPath nickname = createString("nickname");

    public final ListPath<String, StringPath> roles = this.<String, StringPath>createList("roles", String.class, StringPath.class, PathInits.DIRECT2);

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

