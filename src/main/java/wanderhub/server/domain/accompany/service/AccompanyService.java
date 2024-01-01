package wanderhub.server.domain.accompany.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.accompany.dto.*;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany.repository.AccompanyRepository;
import wanderhub.server.domain.accompany.repository.AccompanySearchRepository;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.domain.accompany_member.service.AccompanyMemberService;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.response.PageResponseDto;
import wanderhub.server.global.utils.Local;

import java.text.ParseException;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccompanyService {

    private final MemberService memberService;
    private final AccompanyMemberService accompanyMemberService;
    private final AccompanyRepository accompanyRepository;
    private final AccompanySearchRepository accompanySearchRepository;

    // 동행 생성
    public void createAccompany(AccompanyDto.Post postAccompany, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);               // 통과시 회원 검증 완료

        Accompany initAccompany = Accompany.builder()
                .accompanyMaker(findMember.getNickname())
                .local(Local.getLocal(postAccompany.getLocal()))
                .maxMemberCount(postAccompany.getMaxMemberCount())
                .accompanyStartDate(postAccompany.getAccompanyStartDate())
                .accompanyEndDate(postAccompany.getAccompanyEndDate())
                .title(postAccompany.getTitle())
                .content(postAccompany.getContent())
                .coordinateX(postAccompany.getCoordinateX())
                .coordinateY(postAccompany.getCoordinateY())
                .placeName(postAccompany.getPlaceName())
                .build();

        accompanyMemberService.createAccompanyMember(initAccompany, findMember);// 동행_멤버 생성
        accompanyRepository.save(initAccompany);
    }



    // 동행 생성 - 리팩
    @Transactional // 생성
    public void createAccompanyRefactoring(AccompanyPostDto accompanyPostDto, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);        // 회원찾기 // 회원 있는지 확인 & 회원 닉네임 & 회원 활동중 => JPQL로
        memberService.verificationMember(findMember);               // 통과시 회원 검증 완료
        Accompany createdAccompaney = Accompany.createAccompany(findMember.getNickname(), accompanyPostDto);
        accompanyMemberService.createAccompanyMemberRefactoring(createdAccompaney, findMember);// 동행_멤버 생성
        accompanyRepository.save(createdAccompaney);
    }
    
    // 동행 수정
    public AccompanySingleResponseVO updateAccompany(Long accompanyId, String email, AccompanyPostDto accompanyPostDto) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany findAccompany = verificationAccompanyExists(accompanyId); // 수정될 Accompany
        log.info("동행 존재 여부 확인");
        // 작성자도 같은 사람인지 확인
        verificationWriter(findAccompany, findMember.getNickname());        // 닉네임 확인
        log.info("작성자와 같은 회원인지 확인");
        findAccompany.updateAccompany(accompanyPostDto);

        return accompanySearchRepository.getAccompany(findAccompany.getAccompanyId());
    }

    // 동행 삭제
    public void removeAccompany(Long accompanyId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany willRemoveAccompany = verificationAccompanyExists(accompanyId);    // 삭제될 Accompany
        // 작성자도 같은 사람인지 확인
        verificationWriter(willRemoveAccompany, findMember.getNickname());           // 닉네임 확인
        // Accompany가 존재하고, 동일한 작성자라면, 삭제
        accompanyRepository.delete(willRemoveAccompany);                            // accompany삭제
    }

    // 동행 단일 조회
    public AccompanySingleResponseVO getAccompany(Long accompanyId) {
        verificationAccompanyExists(accompanyId);// 동행 유효성 검증
        return accompanySearchRepository.getAccompany(accompanyId);
    }

    // 동행 전체 조회
    public PageResponseDto<AccompanyListResponseDto> findByLocalAndDate(AccompanySearchCondition accompanySearchCondition, Integer page) throws ParseException {
        return accompanySearchRepository.searchByLocalAndDate(accompanySearchCondition, page);
    }


    // 동행 참여
    public AccompanySingleResponseVO joinAccompany(Long accompanyId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany findAccompany = verificationAccompanyExists(accompanyId);
        // 동행 참여 가능한지 확인,
            // 동행 작성자라면..?
        if(findAccompany.getAccompanyMaker().equals(findMember.getNickname())) {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_WRITER);
        }
            // 내가 이미 참여되지 않은 상태여야 함.
        Optional<AccompanyMember> findAccompanyMember = accompanyMemberService.findAccompanyMember(accompanyId, findMember.getId());
        if(findAccompanyMember.isPresent()) {   // 있으면 참여한 사람
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_JOIN_ALREADY_JOINED);  // 있으니깐 예외처리
        }   // else를 쓰지 않고도 참여했는지 안했는지 검증 가능.
        // 참여된 인원이 max넘버보다 작아야됨, 같으면 예외(큰 경우는 없음)
        if(findAccompany.getAccompanyMemberList().size()==findAccompany.getMaxMemberCount()) {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_JOIN_MAX);
        }
        // 위의 과정이 완료되면 참여가능
        accompanyMemberService.createAccompanyMember(findAccompany, findMember);
        AccompanySingleResponseVO accompanyResponseDto = accompanySearchRepository.getAccompany(accompanyId);
        return accompanyResponseDto;
    }

    // 참여한 동행 나가기
    public AccompanySingleResponseVO outAccompany(Long accompanyId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany findAccompany = verificationAccompanyExists(accompanyId);
        // 동행 나가기 가능한지 확인
            // 작성자라면 불가능
        if(findMember.getNickname().equals(findAccompany.getAccompanyMaker())) {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_WRITER);
        }
        // 참여한 사람인지 아닌지 여부만 확인한 후, 나가게 하면 됨.
        Optional<AccompanyMember> findAccompanyMember = accompanyMemberService.findAccompanyMember(accompanyId, findMember.getId());
        if(!findAccompanyMember.isPresent()) {  // 없다면,
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_CANNOT_QUIT);
        }
        // 위의 모든 검증 통과시
        accompanyMemberService.outAccompanyMember(accompanyId, findMember.getId());
        AccompanySingleResponseVO accompanyResponseDto = accompanySearchRepository.getAccompany(accompanyId);
        return accompanyResponseDto;
    }

    // 동행 모집 완료
    public boolean recruitComplete(Long accompanyId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany findAccompany = verificationAccompanyExists(accompanyId);

        // 동행 모집 완료 조건
            // 작성자만 가능
        verificationWriter(findAccompany, findMember.getNickname());    // 다르면 예외처리 // 모집 완료 여부 결점 가능 조건 검증 완료
        accompanyRepository.setRecruitComplete(findAccompany.getAccompanyId(), !findAccompany.isRecruitComplete());
        return findAccompany.isRecruitComplete();
    }

    // 회원 탈퇴시 회원이 만든 동행은 삭제한다.
    public void removeAccompanyfromMakerMember(Member member) {
        // 다른 회원이 있을 경우, 2번째 회원에게 방장을 양도한다. // 추후
//        List<AccompanyMember> accompanyMemberList = member.getAccompanyMemberList();
        // 동행들 찾고,
            // 멤버가 2명이상인 동행 거르고 방장 양도하는 bulk 연산을하고,
            // 멤버가 1명이면, 동행에 참여한 다른 회원은 없으므로 연관된 동행은 삭제한다.
        // 다른 회원이 없을 경우
        accompanyRepository.deleteAccompanyByAccompanyMaker(member.getNickname());

    }

    // ------------------ 유효성 검증 ------------------

    // 동행 존재여부
    public Accompany verificationAccompanyExists(Long accompanyId) {
        Optional<Accompany> findAccompanyById = accompanyRepository.findById(accompanyId);
        return findAccompanyById.orElseThrow(() -> new CustomLogicException(ExceptionCode.ACCOMPANY_NOT_FOUND));
    }
    
    // 작성자가 동일한 사람인지 확인
    public void verificationWriter(Accompany findAccompany, String nickName) {
        if(!findAccompany.getAccompanyMaker().equals(nickName)) { // 다른 사람이라면,
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_WRITER_DIFFERENT);
        }
    }



}