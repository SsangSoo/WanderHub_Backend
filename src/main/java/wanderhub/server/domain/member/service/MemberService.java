package wanderhub.server.domain.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.auth.utils.CustomAuthorityUtils;
import wanderhub.server.domain.accompany.dto.AccompanyResponseListDto;
import wanderhub.server.domain.board.dto.BoardListResponseDto;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.entity.MemberStatus;
import wanderhub.server.domain.member.repository.MemberRepository;
import wanderhub.server.domain.member.repository.MemberSearchQueryDsl;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.utils.CustomBeanUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;
    private final CustomBeanUtils<Member> customBeanUtils;
    private final MemberSearchQueryDsl memberSearchQueryDsl;

    public MemberService(MemberRepository memberRepository, CustomAuthorityUtils authorityUtils, CustomBeanUtils<Member> customBeanUtils, MemberSearchQueryDsl memberSearchQueryDsl) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
        this.customBeanUtils = customBeanUtils;
        this.memberSearchQueryDsl = memberSearchQueryDsl;
    }

    public Member createMember(Member createMember) {
        createMember.setMemberStatus(MemberStatus.ACTIVE);                    // 멤버 활동 중 변경
        createMember.setRoles(authorityUtils.createRoles(createMember.getEmail())); // 멤버의 이메일로 권한 생성
        return memberRepository.save(createMember);   // 멤버를 DB에 저장한다.
    }

    // 멤버 수정 메서드
    public Member updateMember(Member existingMember, Member memberWithUpdateData) { // 기존멤버 existingMember , 수정할 정보를 가진 member
        verificationActiveMember(existingMember);   // 일단 휴면상태인지 검증
        verificatioinNickName(existingMember, memberWithUpdateData);    // 닉네임 검증
        return customBeanUtils.copyNonNullProoerties(memberWithUpdateData, existingMember);  // updateMember 정보가 >>> srcMember로 업데이트된다.
    }

    // 멤버조회
    public Member getMember(String email) {
        Member findMember = findMember(email);
        verificationMember(findMember);   // 닉네임이 없거나 휴면상태인 회원이 서비스를 시작하려할 때, 검증 메서드
        // 통과되면 멤버 돌려줌.
        return findMember;
    }

    // 멤버를 이메일로 찾는다.
    // 없으면 예외 던진다.
    public Member findMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }


    // 회원가입시 이메일로 멤버 찾는 용도 사용 // OAuth2MemberSuccessHandler에서
    // 회원가입시 이메일로 멤버 찾는 용도 사용
    public Optional<Member> findByEmail(String email) {
        // 이메일로 멤버를 찾아온다.
        return memberRepository.findByEmail(email);
    }


    // 회원탈퇴 => 휴면상태
    public void quitMember(String email) {
        verificationMemberByEmail(email);
        Member member = findMember(email);
        member.setMemberStatus(MemberStatus.HUMAN);
    }

    // 내가 작성한 게시판
    public List<BoardListResponseDto> getWriteBoardList(String email) {
        Member findMember = findMember(email);
        verificationMember(findMember);       // 통과시 회원 검증 완료
        return memberSearchQueryDsl.getWriteBoardList(findMember.getNickName());

    }

    // 내가 좋아요 한 게시판
    public List<BoardListResponseDto> getWriteBoardListWithHeart(String email) {
        Member findMember = findMember(email);
        verificationMember(findMember);       // 통과시 회원 검증 완료
        return memberSearchQueryDsl.getWriteBoardListWithHeart(findMember.getId());
    }

    // 내가 댓글 달은 게시판
    public List<BoardListResponseDto> getBoardWithWriteMyBoardComment(String email) {
        Member findMember = findMember(email);
        verificationMember(findMember);       // 통과시 회원 검증 완료
        return memberSearchQueryDsl.getBoardWithWriteMyBoardComment(findMember.getNickName());
    }

    // 내가 좋아요 달은 댓글이 있는 게시판
    public List<BoardListResponseDto> getBoardWithWriteHeartBoardComment(String email) {
        Member findMember = findMember(email);
        verificationMember(findMember);       // 통과시 회원 검증 완료
        return memberSearchQueryDsl.getBoardWithWriteHeartBoardComment(findMember.getId());
    }

    // 내가 만등 동행
    public List<AccompanyResponseListDto> getWriteAccompanList(String email) {
        Member findMember = findMember(email);
        verificationMember(findMember);       // 통과시 회원 검증 완료
        return memberSearchQueryDsl.getWriteAccompanList(findMember.getNickName());
    }

    // 내가 참여 중인 동행
    public List<AccompanyResponseListDto> getWriteAccompanyJoined(String email) {
        Member findMember = findMember(email);
        verificationMember(findMember);       // 통과시 회원 검증 완료
        return memberSearchQueryDsl.getWriteAccompanyJoined(findMember.getNickName());
    }

    // --------------------------- 유효성 검증----------------------


    // 닉네임 검증 메서드
    public void verificatioinNickName(Member existingMember, Member memberWithUpdateData) {
        if (existingMember.isNewbie()) { // 뉴비라면,
            verificationNewbie(memberWithUpdateData);   // 뉴비검증 닉네임 정보 없으면 예외
            // 뉴비검증 통과시 닉네임 변경될 것이기때문에,
            // 신규회원여부를 false로 변경하여 기존 멤버라고 한다.
            existingMember.setNewbie(false);
        } else {
            verificationNotNewbie(memberWithUpdateData);    // 기존 멤버에 닉네임 변경시도가 있으면 예외 발생
        }
    }

    // 뉴비는 닉네임을 변경해야하는데, 변경이 없다면, 변경하라고 예외발생시켜야함.
    public void verificationNewbie(Member memberWithUpdateData) {
        // 뉴비인데, updateMember에도 닉네임정보가 없다면, 닉네임 변경을 해달라는 예외를 던진다.
        if (memberWithUpdateData.getNickName() == null) {
            throw new CustomLogicException(ExceptionCode.NICKNAME_REQUIRED);
        } else {        // 닉네임이 있는데, 중복이 발생하면 이미 있다고 예외발생시켜야함.
            Optional<Member> nickNameDuplicatedMember = memberRepository.findByNickName(memberWithUpdateData.getNickName());  // 닉네임을 가진 회원
            if (nickNameDuplicatedMember.isPresent()) {
                throw new CustomLogicException(ExceptionCode.NICKNAME_DUPLICATED);  // 값이 있으면 이미 닉네임은 사용되는 사람이므로 예외발생시켜야함.
            }
        }
    }


    // 기존 회원은 닉네임을 변경하려고할 때 예외를 발생시킨다.
    public void verificationNotNewbie(Member memberWithUpdateData) {
        if (memberWithUpdateData.getNickName() != null) { // 기존멤버가 닉네임을 변경하려고하면, 예외 발생
            throw new CustomLogicException(ExceptionCode.NICKNAME_NOT_UPDATE);
        }
    }

    // 닉네임이 없거나 휴면상태인 회원이 서비스를 시작하려할 때, 검증 메서드
    public void verificationMember(Member withoutNickNameOrHumanMember) {
        if (withoutNickNameOrHumanMember.isNewbie() && withoutNickNameOrHumanMember.getNickName() == null) {          // 닉네임이 없는 사람인지 검증
            throw new CustomLogicException(ExceptionCode.NICKNAME_REQUIRED);
        }
        verificationActiveMember(withoutNickNameOrHumanMember);   // 휴면상태 검증
    }

    public void verificationMemberByEmail(String email) {
        verificationMember(findMember(email));  // 이메일로 검증하게하기 // select한번 '덜' 날리기 위해..
    }

    public void verificationActiveMember(Member activeWhetherMember) {      // 맴버활동여부
        if (activeWhetherMember.getMemberStatus() == MemberStatus.HUMAN) {              // 휴면상태인지 확인
            throw new CustomLogicException(ExceptionCode.MEMBER_ALREADY_HUMAN);
        }
    }

}

