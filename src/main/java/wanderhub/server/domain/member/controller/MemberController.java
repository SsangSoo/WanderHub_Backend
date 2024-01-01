package wanderhub.server.domain.member.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.domain.member.dto.MemberDto;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.mapper.MemberMapper;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.response.MessageResponseDto;
import wanderhub.server.global.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final TokenService tokenService;


    // 회원 정보 수정
    @PatchMapping
    public ResponseEntity updateMember(HttpServletRequest request, Principal principal, @RequestBody MemberDto.Patch patch) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token확인
        Member findMember = memberService.findMember(principal.getName());   // 이메일 정보로 사용자를 찾아온다.
        // memberService의 updateMember메서드를 통해 사용자의 정보를 수정한다.   // PatchDto를 mapper를 통해서 엔티티로 매핑한다.
        Member updatedMember = memberService.updateMember(findMember, memberMapper.memberPatchDtoToMemberEntity(patch));
        return ResponseEntity.ok(new SingleResponse<>(memberMapper.memberEntityToMemberResponseDto(updatedMember)));
    }

    // 회원 정보 조회
    @GetMapping
    public ResponseEntity getMember(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token확인
        Member member = memberService.getMember(principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(memberMapper.getMemberEntityToMemberResponseDto(member)));
    }

    // 회원 탈퇴 -> 휴면 상태
    @PatchMapping("/quit")
    public ResponseEntity quitMember(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token확인
        memberService.quitMember(principal.getName());
        return new ResponseEntity(new MessageResponseDto("회원이 휴면계정으로 변경되었습니다."), HttpStatus.NO_CONTENT);
    }


    // 내가 만든 게시판
    @GetMapping("/board/myWriteBoard")
    public ResponseEntity getWriteBoardList(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);
        return ResponseEntity.ok(memberService.getWriteBoardList(principal.getName()));
    }


    // 내가 좋아요 한 게시판
    @GetMapping("/board/myHeart")
    public ResponseEntity getWriteBoardListWithHeart(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);
        return ResponseEntity.ok(memberService.getWriteBoardListWithHeart(principal.getName()));
    }


    // 내가 댓글 달은 게시판
    @GetMapping("/board/with-myComment")
    public ResponseEntity getBoardWithWriteMyBoardComment(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);
        return ResponseEntity.ok(memberService.getBoardWithWriteMyBoardComment(principal.getName()));
    }

    // 내가 좋아요 한 댓글이 있는 게시판
    @GetMapping("/board/with-myCommentAndHeart")
    public ResponseEntity getBoardWithWriteHeartBoardComment(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);
        return ResponseEntity.ok(memberService.getBoardWithWriteHeartBoardComment(principal.getName()));
    }

    // 내가 만등 동행
    @GetMapping("/accompany/myWrite")
    public ResponseEntity getWriteAccompanyList(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);
        return ResponseEntity.ok(memberService.getWriteAccompanList(principal.getName()));
    }

    // 내가 참여 중인 동행
    @GetMapping("/accompany/JoinedMe")
    public ResponseEntity getWriteAccompanyJoined(HttpServletRequest request, Principal principal) {
        tokenService.verificationLogOutToken(request);
        return ResponseEntity.ok(memberService.getWriteAccompanyJoined(principal.getName()));
    }
}