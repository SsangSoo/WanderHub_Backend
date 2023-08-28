package wanderhub.server.domain.accompany.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.accompany.dto.AccompanyResponseListDto;
import wanderhub.server.domain.accompany.dto.AccompanySearchCondition;
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
import wanderhub.server.global.utils.CustomBeanUtils;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AccompanyService {

    private final MemberService memberService;
    private final AccompanyMemberService accompanyMemberService;
    private final AccompanyRepository accompanyRepository;
    private final CustomBeanUtils<Accompany> customBeanUtils;
    private final AccompanySearchRepository accompanySearchRepository;

    public AccompanyService(MemberService memberService, AccompanyMemberService accompanyMemberService, AccompanyRepository accompanyRepository, CustomBeanUtils<Accompany> customBeanUtils, AccompanySearchRepository accompanySearchRepository) {
        this.memberService = memberService;
        this.accompanyMemberService = accompanyMemberService;
        this.accompanyRepository = accompanyRepository;
        this.customBeanUtils = customBeanUtils;
        this.accompanySearchRepository = accompanySearchRepository;
    }

    // 동행 생성
    public AccompanyResponseDto createAccompany(Accompany postAccompany, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);               // 통과시 회원 검증 완료
        postAccompany.setAccompanyInit(findMember.getNickName());   // 동행 생성자 닉네임 설정
        Accompany savedAcoompany = accompanyRepository.save(postAccompany);     // 동행 생성
        accompanyMemberService.createAccompanyMember(savedAcoompany, findMember);// 동행_멤버 생성
        AccompanyResponseDto accompanyResponseDto = accompanySearchRepository.getAccompany(savedAcoompany.getAccompanyId());
        return accompanyResponseDto;
    }
    
    // 동행 수정
    public AccompanyResponseDto updateAccompany(Long accompanyId, String email, AccompanyDto.Patch accompanyPatchDto) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany willWriteAccompany = verificationAccompanyExists(accompanyId);    // 수정될 Accompany
        // 작성자도 같은 사람인지 확인
        verificationWriter(willWriteAccompany, findMember.getNickName());           // 닉네임 확인
        willWriteAccompany.updateAccompany(accompanyPatchDto);
        AccompanyResponseDto accompanyResponseDto = accompanySearchRepository.getAccompany(accompanyId);
        return accompanyResponseDto;
    }

    // 동행 삭제
    public void removeAccompany(Long accompanyId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany willRemoveAccompany = verificationAccompanyExists(accompanyId);    // 삭제될 Accompany
        // 작성자도 같은 사람인지 확인
        verificationWriter(willRemoveAccompany, findMember.getNickName());           // 닉네임 확인
        // Accompany가 존재하고, 동일한 작성자라면, 삭제
        accompanyRepository.delete(willRemoveAccompany);                            // accompany삭제
        accompanyMemberService.removeAccompanyMemberByAccompanyId(accompanyId);     // AccompanyMember 삭제
    }

    // 동행 단일 조회
    public AccompanyResponseDto getAccompany(Long accompanyId) {
        verificationAccompanyExists(accompanyId);  // 동행 유효성 검증
        AccompanyResponseDto accompanyResponseDto = accompanySearchRepository.getAccompany(accompanyId);
        return accompanyResponseDto;
    }

    // 동행 전체 조회
    public PageResponseDto<AccompanyResponseListDto> findByLocalAndDate(AccompanySearchCondition accompanySearchCondition, Integer page) throws ParseException {
        return accompanySearchRepository.searchByLocalAndDate(accompanySearchCondition, page);
    }


    // 동행 참여
    public AccompanyResponseDto joinAccompany(Long accompanyId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany findAccompany = verificationAccompanyExists(accompanyId);
        // 동행 참여 가능한지 확인,
            // 동행 작성자라면..?
        if(findAccompany.getNickname().equals(findMember.getNickName())) {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_WRITER);
        }
            // 내가 이미 참여되지 않은 상태여야 함.
        Optional<AccompanyMember> findAccompanyMember = accompanyMemberService.findAccompanyMember(accompanyId, findMember.getId());
        if(findAccompanyMember.isPresent()) {   // 있으면 참여한 사람
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_JOIN_ALREADY_JOINED);  // 있으니깐 예외처리
        }   // else를 쓰지 않고도 참여했는지 안했는지 검증 가능.
        // 참여된 인원이 max넘버보다 작아야됨, 같으면 예외(큰 경우는 없음)
        if(findAccompany.getAccompanyMemberList().size()==findAccompany.getMaxMemberNum()) {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_JOIN_MAX);
        }
        // 위의 과정이 완료되면 참여가능
        accompanyMemberService.createAccompanyMember(findAccompany, findMember);
        AccompanyResponseDto accompanyResponseDto = accompanySearchRepository.getAccompany(accompanyId);
        return accompanyResponseDto;
    }

    // 참여한 동행 나가기
    public AccompanyResponseDto outAccompany(Long accompanyId, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인,
        Accompany findAccompany = verificationAccompanyExists(accompanyId);
        // 동행 나가기 가능한지 확인
            // 작성자라면 불가능
        if(findMember.getNickName().equals(findAccompany.getNickname())) {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_WRITER);
        }
        // 참여한 사람인지 아닌지 여부만 확인한 후, 나가게 하면 됨.
        Optional<AccompanyMember> findAccompanyMember = accompanyMemberService.findAccompanyMember(accompanyId, findMember.getId());
        if(!findAccompanyMember.isPresent()) {  // 없다면,
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_CANNOT_QUIT);
        }
        // 위의 모든 검증 통과시
        accompanyMemberService.outAccompanyMember(accompanyId, findMember.getId());
        AccompanyResponseDto accompanyResponseDto = accompanySearchRepository.getAccompany(accompanyId);
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
        verificationWriter(findAccompany, findMember.getNickName());    // 다르면 예외처리 // 모집 완료 여부 결점 가능 조건 검증 완료
        findAccompany.setRecruitComplete();
        return findAccompany.isRecruitComplete();
    }

    // ------------------ 유효성 검증 ------------------

    // 동행 존재여부
    public Accompany verificationAccompanyExists(Long accompanyId) { 
        Optional<Accompany> findedAccompanyById = accompanyRepository.findById(accompanyId);
        return findedAccompanyById.orElseThrow(() -> new CustomLogicException(ExceptionCode.ACCOMPANY_NOT_FOUND));
    }
    
    // 작성자가 동일한 사람인지 확인
    public void verificationWriter(Accompany writerVerifiacationAccompany, String nickName) {
        if(!writerVerifiacationAccompany.getNickname().equals(nickName)) { // 다른 사람이라면,
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_WRITER_DIFFERENT);
        }
    }


}