package wanderhub.server.domain.community.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.community.entity.Board;
import wanderhub.server.domain.community.repository.BoardRepository;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.utils.CustomBeanUtils;

import java.util.Optional;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final CustomBeanUtils<Board> customBeanUtils;
    private final MemberService memberService;

    public BoardService(BoardRepository boardRepository, CustomBeanUtils<Board> customBeanUtils, MemberService memberService) {
        this.boardRepository = boardRepository;
        this.customBeanUtils = customBeanUtils;
        this.memberService = memberService;
    }

    public Board createBoard(Board board, String email) {   // 컨트롤러부터 매퍼로 매핑된 Board와 인증객체의 이메일을 찾아온다.
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        board.setNickName(findMember.getNickName());        // 작성자
        board.setViewPoint(0L);                             // 조회수 0부터
        board.setMember(findMember);
        return boardRepository.save(board);
    }

    // 게시판 수정
    public Board updateBoard(Long boardId, Board patchBoard, String email) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        Board findBoard = verificationBoard(boardId);         // 게시판을 찾아온다. // 없으면 예외를 발생시킨다.
        verificationWriter(findBoard, findMember);            // 닉네임으로 게시판 수정을 시도하는 사용자가 작성자와 동일한지 검증
        return customBeanUtils.copyNonNullProoerties(patchBoard, findBoard);
    }

    // 게시판 삭제
    public void removeBoard(Long boardId,String email) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);               // 통과시 회원 검증 완료
        Board findBoard = verificationBoard(boardId);                   //  게시판이 있는 게시판인지 확인
        verificationWriter(findBoard, findMember);                      //  삭제시도를 하는 사람이 작성한 사람인지 확인 // 통과되면 검증 완료
        boardRepository.delete(findBoard);                              //  개시판 삭제
    }

    // 게시판 단일 조회
    public Board getBoard(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if(board.isPresent()) { // 게시판이 있으면
            Board findBoard = board.get();  // 실제 게시판 객체 추출
            Long currentViewPoint = findBoard.getViewPoint(); // 현재 조회수를 추출한다.
            findBoard.setViewPoint(currentViewPoint+1L);      // 조회수를 1 증가시킨다. // Long타입이기때문에 1L.
            return findBoard;   // Board를 반환한다.
        } else {
            throw new CustomLogicException(ExceptionCode.BOARD_NOT_FOUND);  // 없으면 예외던진다.
        }
    }

    // 게시판 전체 조회
    public Page<Board> findBorrows(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 게시판을 찾을 떄 없으면 예외 발생
    public Board verificationBoard(Long boardId) {
        Optional<Board> findedBoard = boardRepository.findById(boardId);   // 게시판을 찾는다.
        return findedBoard.orElseThrow(() ->                                // 찾은 게시판을 리턴한다.
                new CustomLogicException(ExceptionCode.BOARD_NOT_FOUND));   // 찾지 못하면 예외를 발생시킨다.
    }

    // 게시판 작성자와 수정하려는 사람이 같은 사람인지 확인해주는 메서드               // 닉네임으로 확인한다.
    public void verificationWriter(Board board, Member member) {
        if (!board.getNickName().equals(member.getNickName())) {            // 찾은 게시판의 작성자와 게시판을 수정을 시도하려는 사용자의 닉네임이 다를 때
            throw new CustomLogicException(ExceptionCode.BOARD_WRITER_DIFFERENT);   // 멤버가 다르다는 예외 발생

        }
    }




}
