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
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
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

    //생성
    @PostMapping
    public ResponseEntity create(Principal principal, @Validated @RequestBody AccompanyDto accompanyDto) {
        Accompany entityReq = AccompanyMapper.INSTANCE.toEntity(accompanyDto); //requestDto -> entity
        entityReq.setAccompanyDate(LocalDate.parse(accompanyDto.getAccompanyDate())); //accompanyDate 형변환 (String->LocalDate)
        Accompany entityResp = accompanyService.createAccompany(entityReq, principal.getName()); //생성
        AccompanyResponseDto dto = AccompanyMapper.INSTANCE.toDto(entityResp); //entity -> responseDto
        //AccompanyMember에 인원수 1명(만든 사람) 추가
        accompanyMemberService.createAccompanyMember(entityResp.getId(), memberService.findByEmail(principal.getName()).get().getId());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //전체 조회 (경로 임시)
    @GetMapping
    public ResponseEntity<List<AccompanyResponseDto>> findAll(@PageableDefault(sort = "id") Pageable pageable) {
        Page<Accompany> entityPage = accompanyService.findAll(pageable);
        List<Accompany> entityList = entityPage.getContent();
        List<AccompanyResponseDto> dtoList = AccompanyMapper.INSTANCE.toDtoList(entityList);

        return ResponseEntity.ok(dtoList);
    }

    //accompanyId로 조회
    @GetMapping("/{id}")
    public ResponseEntity<Optional<AccompanyResponseDto>> findById(@PathVariable Long id) {
        Accompany entity = accompanyService.findById(id).get();
        AccompanyResponseDto dto = AccompanyMapper.INSTANCE.toDto(entity);

        return ResponseEntity.ok(Optional.of(dto));
    }

    //지역 별 조회
    @GetMapping("/bylocal")
    public ResponseEntity<List<AccompanyResponseDto>> findByLocal(@RequestParam(value = "accompanyLocal") String local,
                                                                  @PageableDefault(sort = "id") Pageable pageable) {
        List<Accompany> ent = accompanyService.findByLocal(local, pageable).getContent();
        List<AccompanyResponseDto> dtoList = AccompanyMapper.INSTANCE.toDtoList(ent);

        return ResponseEntity.ok(dtoList);
    }

    //일
    @GetMapping("/bydate")
    public ResponseEntity<List<AccompanyResponseDto>> findByDate(@RequestParam(value = "accompanyDate") String date,
                                                                 @PageableDefault(sort = "id") Pageable pageable) {
        List<Accompany> entityList = accompanyService.findByDate(date, pageable).getContent();
        List<AccompanyResponseDto> dtoList = AccompanyMapper.INSTANCE.toDtoList(entityList);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/bylocalanddate")
    public ResponseEntity<List<AccompanyResponseDto>> findByLocalAndDate(@RequestParam(value = "accompanyLocal") String local,
                                                                         @RequestParam(value = "accompanyDate") String date,
                                                                         @PageableDefault(sort = "id") Pageable pageable) {
        List<Accompany> entityList = accompanyService.findByLocalAndDate(local, date, pageable).getContent();
        List<AccompanyResponseDto> dtoList = AccompanyMapper.INSTANCE.toDtoList(entityList);
        return ResponseEntity.ok(dtoList);
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
