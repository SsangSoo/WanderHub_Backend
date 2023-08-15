package wanderhub.server.domain.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.board.dto.BoardListResponseDto;
import wanderhub.server.domain.board.dto.BoardResponseDto;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.board.repository.BoardRepository;
import wanderhub.server.domain.board.repository.BoardQueryDslRepository;
import wanderhub.server.domain.board_heart.entity.BoardHeart;
import wanderhub.server.domain.board_heart.service.BoardHeartService;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.response.PageResponseDto;
import wanderhub.server.global.utils.CustomBeanUtils;

import java.util.Optional;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final CustomBeanUtils<Board> customBeanUtils;
    private final MemberService memberService;
    private final BoardHeartService boardHeartService;
    private final BoardQueryDslRepository boardQueryDslRepository;


    public BoardService(BoardRepository boardRepository, CustomBeanUtils<Board> customBeanUtils, MemberService memberService, BoardHeartService boardHeartService, BoardQueryDslRepository boardQueryDslRepository) {
        this.boardRepository = boardRepository;
        this.customBeanUtils = customBeanUtils;
        this.memberService = memberService;
        this.boardHeartService = boardHeartService;
        this.boardQueryDslRepository = boardQueryDslRepository;
    }

    public BoardResponseDto createBoard(Board board, String email) {   // 컨트롤러부터 매퍼로 매핑된 Board와 인증객체의 이메일을 찾아온다.
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        board.setBoardInit(findMember);                     // 멤버와 닉네임 한 번에 설정
        Board savedBoard = boardRepository.save(board);
        return boardQueryDslRepository.getResultBoard(savedBoard.getBoardId());

    }

    // 게시판 수정
    public BoardResponseDto updateBoard(Long boardId, Board patchBoard, String email) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);         // 통과시 회원 검증 완료
        Board findBoard = verificationBoard(boardId);         // 게시판을 찾아온다. // 없으면 예외를 발생시킨다.
        verificationWriter(findBoard, findMember);            // 닉네임으로 게시판 수정을 시도하는 사용자가 작성자와 동일한지 검증
        Board updatedBoard = customBeanUtils.copyNonNullProoerties(patchBoard, findBoard);
        return boardQueryDslRepository.getResultBoard(updatedBoard.getBoardId());
    }

    // 게시판 삭제
    public void removeBoard(Long boardId,String email) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);               // 통과시 회원 검증 완료
        Board findBoard = verificationBoard(boardId);                   //  게시판이 있는 게시판인지 확인
        verificationWriter(findBoard, findMember);                      //  삭제 시도를 하는 사람이 작성한 사람인지 확인 // 통과되면 검증 완료
        boardRepository.delete(findBoard);                              //  개시판 삭제
    }


    // Id로 게시판 찾기
    public Board findById(Long boardId) {
        Optional<Board> findByIdBoard = boardRepository.findById(boardId);
        return findByIdBoard.orElseThrow(() ->
                new CustomLogicException(ExceptionCode.BOARD_NOT_FOUND));
    }

    // 게시판 단일 조회
    public Board getTempBoard(Long boardId) {
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

    // 게시판 단일 조회 N + 1 해결
    public BoardResponseDto getBoard(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if(board.isPresent()) { // 게시판이 있으면
            Board findBoard = board.get();  // 실제 게시판 객체 추출
            boardQueryDslRepository.updateViewPoint(findBoard.getBoardId());  // viewPoint 1 증가.
            BoardResponseDto resultBoard = boardQueryDslRepository.getResultBoard(findBoard.getBoardId()); // Board Dto를 반환
            return resultBoard;  // Board를 반환
        } else {
            throw new CustomLogicException(ExceptionCode.BOARD_NOT_FOUND);  // 없으면 예외던진다.
        }
    }

    // 게시판 전체 조회
    public Page<Board> findBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 게시판 전체 조회 N+1 해결
    public PageResponseDto<BoardListResponseDto> findAllBoards(Integer page) {
        return boardQueryDslRepository.searchBoard(page);
    }

    // 게시판 좋아요
    public BoardResponseDto likeBoard(Long boardId, String email) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        Board findBoard = verificationBoard(boardId);         // 게시판을 찾아온다. // 없으면 예외를 발생시킨다.
        // 좋아요 테이블에서 boardId와 email을 통해서 좋아요 Entity를 Optional로 받는다.
        Optional<BoardHeart> findBoardHeart = boardHeartService.findByMemberAndBoard(boardId, email);
        // 없으면, 좋아요 생성
        if(!findBoardHeart.isPresent()) {
            boardHeartService.createBoardHeart(findMember, findBoard);
        } else {    // 있으면 삭제   // 회원 이메일과 게시판 Id를 명확하게 주고 찾아온 Heart Entity라 검증 로직 따로 X.
            boardHeartService.removeBoardHeart(findBoardHeart.get());
        }
        return boardQueryDslRepository.getResultBoard(findBoard.getBoardId());
    }


    // 게시판을 찾을 때 없으면 예외 발생
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
