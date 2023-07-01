package wanderhub.server.domain.accompany.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany.mapper.AccompanyMapper;
import wanderhub.server.domain.accompany.service.AccompanyService;
import wanderhub.server.domain.accompany_member.service.AccompanyMemberService;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/accompany")
@RequiredArgsConstructor
public class AccompanyController {

    private final AccompanyService accompanyService;
    private final AccompanyMemberService accompanyMemberService;
    private final MemberService memberService;
    private final AccompanyMapper mapper;

    //생성
    @PostMapping
    public ResponseEntity create(Principal principal, @Validated @RequestBody AccompanyDto.Post postDto) {
        Accompany entityReq = mapper.accompanyPostDtoToAccompanyEntity(postDto);
        entityReq.setAccompanyDate(LocalDate.parse(postDto.getAccompanyDate())); //accompanyDate 형변환 (String->LocalDate)
        Accompany entityResp = accompanyService.createAccompany(entityReq, principal.getName()); //생성
        AccompanyDto.Response dto = mapper.accompanyEntityToAccompanyResponseDto(entityResp);
        //AccompanyMember에 인원수 1명(만든 사람) 추가
        accompanyMemberService.createAccompanyMember(entityResp.getId(), memberService.findByEmail(principal.getName()).get().getId());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //전체 조회 (경로 임시)
    @GetMapping
    public ResponseEntity findAll(@PageableDefault(sort = "id") Pageable pageable) {
        Page<Accompany> entityPage = accompanyService.findAll(pageable);
        List<Accompany> entityList = entityPage.getContent();
        List<AccompanyDto.Response> dtoList = mapper.accompanyListToAccompanyResponseDtoList(entityList);

        return ResponseEntity.ok(dtoList);
    }

    //accompanyId로 조회
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        Accompany entity = accompanyService.findById(id).get();
        AccompanyDto.Response dto = mapper.accompanyEntityToAccompanyResponseDto(entity);

        return ResponseEntity.ok(Optional.of(dto));
    }

    //지역 별 조회
    @GetMapping("/bylocal")
    public ResponseEntity findByLocal(@RequestParam(value = "accompanyLocal") String local,
                                                                  @PageableDefault(sort = "id") Pageable pageable) {
        List<Accompany> accompanyList = accompanyService.findByLocal(local, pageable).getContent();
        List<AccompanyDto.Response> responseDtoList = mapper.accompanyListToAccompanyResponseDtoList(accompanyList);

        return ResponseEntity.ok(responseDtoList);
    }

    //일
    @GetMapping("/bydate")
    public ResponseEntity findByDate(@RequestParam(value = "accompanyDate") String date,
                                                                 @PageableDefault(sort = "id") Pageable pageable) {
        List<Accompany> accompanyList = accompanyService.findByDate(date, pageable).getContent();
        List<AccompanyDto.Response> responseDtoList = mapper.accompanyListToAccompanyResponseDtoList(accompanyList);
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/bylocalanddate")
    public ResponseEntity findByLocalAndDate(@RequestParam(value = "accompanyLocal") String local,
                                                                         @RequestParam(value = "accompanyDate") String date,
                                                                         @PageableDefault(sort = "id") Pageable pageable) {
        List<Accompany> accompanyList = accompanyService.findByLocalAndDate(local, date, pageable).getContent();
        List<AccompanyDto.Response> responseDtoList = mapper.accompanyListToAccompanyResponseDtoList(accompanyList);
        return ResponseEntity.ok(responseDtoList);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccompany(Principal principal, @PathVariable Long id) {
        accompanyService.deleteAccompany(id, principal.getName());
    }

    //남이 생성한 동행에 참여하기 기능
    @PostMapping("/join/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinAccompany(Principal principal, @PathVariable Long id) {
        String userEmail = principal.getName();
        Member member = memberService.findByEmail(userEmail).get();

        accompanyMemberService.createAccompanyMember(id, member.getId());
    }

    //남이 생성한 동행에서 나오기 기능
    @DeleteMapping("/quit/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void quitAccompany(Principal principal, @PathVariable Long id) {
        String userEmail = principal.getName();
        Member member = memberService.findByEmail(userEmail).get();

        accompanyMemberService.deleteAccompanyMember(id, member.getId());
    }

}
