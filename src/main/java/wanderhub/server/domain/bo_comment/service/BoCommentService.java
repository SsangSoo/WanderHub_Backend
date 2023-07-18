package wanderhub.server.domain.bo_comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;
import wanderhub.server.domain.bo_comment.repository.BoCommentRepository;
import wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart;
import wanderhub.server.domain.bo_comment_heart.service.BoCommentHeartService;
import wanderhub.server.domain.board.service.BoardService;
import wanderhub.server.domain.bo_comment.entity.BoComment;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.utils.CustomBeanUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoCommentService {

    private final MemberService memberService;
    private final CustomBeanUtils<BoComment> customBeanUtils;
    private final BoCommentRepository boCommentRepository;
    private final BoardService boardService;
    private final BoCommentHeartService boCommentHeartService;

    public BoCommentService(MemberService memberService, CustomBeanUtils<BoComment> customBeanUtils, BoCommentRepository boCommentRepository, BoardService boardService, BoCommentHeartService boCommentHeartService) {
        this.memberService = memberService;
        this.customBeanUtils = customBeanUtils;
        this.boCommentRepository = boCommentRepository;
        this.boardService = boardService;
        this.boCommentHeartService = boCommentHeartService;
    }

    // 게시판 댓글 작성
    public BoComment createComment(Long boardId, BoComment createdBoCommentFromPostDto, String email) { // 게시판 Id, 매퍼로부터 생성된 BoComment, 사용자 이메일
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 보드가 존재하는지 확인 후, BoComment 초기 셋팅 메서드 이용.
        createdBoCommentFromPostDto.setBoardInit(boardService.verificationBoard(boardId), findMember);    // Board 확인
        return boCommentRepository.save(createdBoCommentFromPostDto);
    }

    // 게시판 댓글 수정
    public BoComment updateComment(Long boardId, Long boCommentId, BoComment patchBoCommentFromPatchDto, String email) { // 게시판 Id, 댓글 Id, 매퍼로부터 생성된 BoComment, 사용자 이메일
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        boardService.verificationBoard(boardId);            // Board 확인
        // 댓글 확인
        BoComment findBoComment = verificationBoComment(boardId, boCommentId);   // Id로 댓글을 조회한다.
        verficationBoCommentWriter(findBoComment, findMember);          // 댓글과 멤버로 작성자 검증 통과시 수정 가능
        return customBeanUtils.copyNonNullProoerties(patchBoCommentFromPatchDto, findBoComment); // 매퍼로부터 받은 댓글 수정정보 -> 이미 있던 댓글
    }

    // 게시판 댓글 삭제
    public void removeBoComment(Long boardId, Long boCommentId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        boardService.verificationBoard(boardId);            // Board 확인
        // 댓글 확인
        BoComment findBoComment = verificationBoComment(boardId, boCommentId);   // Id로 댓글을 조회한다.
        verficationBoCommentWriter(findBoComment, findMember);          // 댓글과 멤버로 작성자 검증 // 통과시 삭제 가능
        boCommentRepository.delete(findBoComment);
    }

    // 게시판 댓글 좋아요
    public BoComment likeBoCommnet(Long boardId, Long boCommentId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        boardService.verificationBoard(boardId);            // Board 확인
        // 댓글 확인
        BoComment findBoComment = verificationBoComment(boardId, boCommentId);   // Id로 댓글을 조회한다.
        // BoCommentHeart를 찾아온다. // 식별자를 이용해서 Optional객체로 찾아옴.
        Optional<BoCommentHeart> boCommentHeartByBoCommentAndMember = boCommentHeartService.findByBoCommentAndMember(findBoComment.getBoCommentId(), findMember.getId());
        // 있으면 삭제
        if(boCommentHeartByBoCommentAndMember.isPresent()) {
            boCommentHeartService.removeBoCommentHeart(boCommentHeartByBoCommentAndMember.get());
        } else {        // 없으면 생성
            boCommentHeartService.createBoCommentHeart(findMember, findBoComment);
        }
        return findBoComment;
    }

    // =================== 아래부터 유효성 검증 ===================

    public BoComment verificationBoComment(Long boardId, Long boCommentId) {    // 댓글이 있는지 확인한다.
        Optional<BoComment> findBoComment = boCommentRepository.findByBoCommentId(boardId, boCommentId);
        // 있으면 리턴, 없으면 없다고 예외처리한다.
        return findBoComment.orElseThrow(() -> new CustomLogicException(ExceptionCode.BOARD_COMMENT_NOT_FOUND));
    }

    public void verficationBoCommentWriter(BoComment boComment, Member member) {    // 댓글의 작성자를 비교한다.
        if(!boComment.getNickName().equals(member.getNickName()))  {    // 같은 사람이 아니면,
            throw new CustomLogicException(ExceptionCode.BOARD_COMMENT_WRITER_DIFFERENT);
        } // 맞으면 수정 가능.
    }

}
