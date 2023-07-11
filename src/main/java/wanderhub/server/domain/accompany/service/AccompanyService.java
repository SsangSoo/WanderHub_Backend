package wanderhub.server.domain.accompany.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany.repository.AccompanyRepository;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.domain.accompany_member.service.AccompanyMemberService;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.utils.CustomBeanUtils;

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

    public AccompanyService(MemberService memberService, AccompanyMemberService accompanyMemberService, AccompanyRepository accompanyRepository, CustomBeanUtils<Accompany> customBeanUtils) {
        this.memberService = memberService;
        this.accompanyMemberService = accompanyMemberService;
        this.accompanyRepository = accompanyRepository;
        this.customBeanUtils = customBeanUtils;
    }

    // 동행 생성
    public Accompany createAccompany(Accompany postAccompany, String email) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);               // 통과시 회원 검증 완료
        postAccompany.setAccompanyInit(findMember.getNickName());   // 동행 생성자 닉네임 설정
        accompanyMemberService.createAccompanyMember(postAccompany, findMember);// 동행_멤버 생성
        return accompanyRepository.save(postAccompany);
    }
    
    // 동행 수정
    public Accompany updateAccompany(Long accompanyId, String email, Accompany accompanyEntityFromPatchDto) {
        // 이메일을 통해서 사용자의 닉네임이 있는지 없는지 확인한다. // 즉, 사용자 검증을 해준다.
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // 동행이 있는지 확인, 
        Accompany willWriteAccompany = verificationAccompanyExists(accompanyId);    // 수정될 Accompany
        // 작성자도 같은 사람인지 확인
        verificationWriter(willWriteAccompany, findMember.getNickName());           // 닉네임 확인
        return customBeanUtils.copyNonNullProoerties(accompanyEntityFromPatchDto, willWriteAccompany);  // src -> dest로 업데이트  
        
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
    public Accompany getAccompany(Long accompanyId) {
        Accompany getAccompany = verificationAccompanyExists(accompanyId);  // 동행 유효성 검증
        return getAccompany;
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


    public Page<Accompany> findAccompanies(Pageable pageable) {
        return accompanyRepository.findAll(pageable);
    }

}
