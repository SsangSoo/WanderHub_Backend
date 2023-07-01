package wanderhub.server.domain.accompany.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany.repository.AccompanyRepository;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class AccompanyService {

    private final AccompanyRepository accompanyRepository;
    private final MemberService memberService;

    public AccompanyService(AccompanyRepository accompanyRepository, MemberService memberService) {
        this.accompanyRepository = accompanyRepository;
        this.memberService = memberService;
    }

    public Accompany createAccompany(Accompany accompany, String userEmail) {
        Member member = memberService.findMember(userEmail);
        memberService.verificationMember(member); //회원 검증
        accompany.setNickname(member.getNickName());
        accompany.setOpenStatus(true);
        return accompanyRepository.save(accompany);
    }

    public Page<Accompany> findAll(Pageable pageable) {
        return accompanyRepository.findAll(pageable);
    }

    public Optional<Accompany> findById(Long id) {
        Optional<Accompany> accompany = accompanyRepository.findById(id);
        if(accompany.isPresent()) {
            return accompany;
        } else {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_NOT_FOUND);
        }
    }

    public Page<Accompany> findByLocal(String accompanyLocal, Pageable pageable) {
        return accompanyRepository.findByAccompanyLocal(accompanyLocal, pageable);
    }
//    @Override
//    public List<Accompany> findByLocal(String accompanyLocal, Pageable pageable) {
//        return accompanyRepository.findByAccompanyLocal(accompanyLocal, pageable);//.getContent();
//    }

    public Page<Accompany> findByDate(String accompanyDate, Pageable pageable) {
        return accompanyRepository.findByAccompanyDate(LocalDate.parse(accompanyDate), pageable);
    }

    public Page<Accompany> findByLocalAndDate(String accompanyLocal, String accompanyDate, Pageable pageable) {
        return accompanyRepository.findByAccompanyLocalAndAccompanyDate(accompanyLocal, LocalDate.parse(accompanyDate), pageable);
    }

    public void deleteAccompany(Long id, String userEmail) {
        Member member = memberService.findByEmail(userEmail).get();
        memberService.verificationMember(member); //회원 검증

        Accompany accompany = accompanyRepository.findById(id)
                .orElseThrow(()->new CustomLogicException(ExceptionCode.ACCOMPANY_NOT_FOUND));

        if(member.getNickName().equals(accompany.getNickname())) { //생성한 사람만 지울 수 있음
            accompanyRepository.delete(accompany);
        } else {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_WRITER_DIFFERENT);
        }
    }
}
